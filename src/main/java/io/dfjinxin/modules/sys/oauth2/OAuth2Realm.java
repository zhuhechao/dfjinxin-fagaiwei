/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.oauth2;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.dfjinxin.common.utils.RedisUtil;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.entity.UrlEnum;
import io.dfjinxin.modules.sys.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${auth.auth_url}")
    private String authUrl;
    @Value("${auth.client_id}")
    private String clientId;

    @Value("${auth.client_secret}")
    private String clientSecret;

    private Logger logger = LoggerFactory.getLogger(OAuth2Realm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)角色的权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        String userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)用户角色信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String accessToken = (String) token.getCredentials();

        //根据accessToken，查询用户信息
        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        //计算全局token有效时长
        long parm = 0L;
        Map<Object, Object> acceToken = new HashMap<>();
        try{
            acceToken = (Map<Object, Object>) redisUtil.hmget("AccessToken" + accessToken);
            if(!ObjectUtils.isEmpty(acceToken)){
                Date parse1 = (Date) acceToken.get("token_time");
                Date parse = new Date();
                parm = (parse.getTime()-parse1.getTime())/1000 - 900;
            }
        }catch (Exception e){
            logger.info("获取Redis中的数据报错",e.getMessage());
        }
        //token失效
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            //进行全局退出，清空全局token
            try{
                redisUtil.deleteCache("AccessToken" + accessToken,"RefreshToken" + accessToken);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
                formData.add("redirctToUrl", "");
                formData.add("redirectToLogin", "authorization_code");
                formData.add("entityId", "5645");
                restTemplate.exchange(authUrl + UrlEnum.LOGOUT_TOKEN_URL.getUrl(), HttpMethod.POST,
                        new HttpEntity< MultiValueMap<String, String>>(formData,headers), String.class).getBody();
            }catch (Exception e){
                logger.info("清除token，错误信息",e.getMessage());
            }
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }else  if(tokenEntity != null  && tokenEntity.getExpireTime().getTime() > System.currentTimeMillis()&&  parm >= 0L ){
            //获取token
            try{
            String aToken = (String) acceToken.get("access_token");
            Map<Object, Object> refToken =  redisUtil.hmget("RefreshToken" + accessToken);
            String refreshToken = (String) refToken.get("refresh_token");
            //判断正式token有效性，刷新token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String userInfo= authUrl + UrlEnum.CHECK_TOKEN_URL.getUrl()+"?access_token="+aToken;
            String tokenBody = restTemplate.exchange(userInfo, HttpMethod.GET,
                    new HttpEntity<MultiValueMap<String, String>>(null,headers), String.class).getBody();
            JSONObject jsonObject = JSONObject.parseObject(tokenBody);
            if( jsonObject.get("errcode").equals("2002")){
                //刷新token，并更新token
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
                formData.add("client_id", clientId);
                formData.add("grant_type", "refresh_token");
                formData.add("refresh_token", refreshToken);
                formData.add("client_secret", clientSecret);
                String codeBody = restTemplate.exchange(authUrl + UrlEnum.REFRESH_TOKEN_URL.getUrl(), HttpMethod.POST,
                        new HttpEntity< MultiValueMap<String, String>>(formData,headers), String.class).getBody();
                JSONObject refreshObject = JSONObject.parseObject(codeBody);
                if(!refreshObject.containsKey("errcode")){
                    String acToken = (String) refreshObject.get("access_token");
                    String reToken = (String) refreshObject.get("refresh_token");
                    Map acMap = new HashMap();
                    acMap.put("access_token",acToken);
                    acMap.put("token_time",new Date());
                    redisUtil.hmset("AccessToken"+accessToken, acMap );
                    //更新redis中的值
                    Map rfMap = new HashMap();
                    rfMap.put("refresh_token",reToken);
                    rfMap.put("token_time",new Date());
                    redisUtil.hmset("RefreshToken"+accessToken, rfMap );
                }
            }
        }catch (Exception e){
                logger.info("刷新全局token报错",e.getMessage());
            }
        }

        SysUserEntity userTmp =  ShiroUtils.getUserEntity();

        if(userTmp==null){
            logger.info("Shiro 缓存中未找到用户信息，从shiro session中获取");
            userTmp = (SysUserEntity) ShiroUtils.getSessionAttribute("user");
            ShiroUtils.setSessionAttribute("user",null);
        }

        if(userTmp==null){
            logger.info("Shiro 缓存、Session中未找到用户信息，从login信息中获取");
            Object principalObj = token.getPrincipal();
            if(token.getPrincipal()!=null){
                userTmp =  (SysUserEntity)  principalObj;
            }
        }

        if(userTmp==null){
            logger.info("Shiro 缓存、Session、login中未找到用户信息，从数据库中获取");
            userTmp = shiroService.queryUser(tokenEntity.getUserId());
//            userTmp.setUserId(userTmp.getSalt());
//            userTmp.setSalt(null);
       //     shiroService.removeUserDBCache(tokenEntity.getUserId());
        }

        logger.info("get sessoin information {}",new Gson().toJson(userTmp));

//        //查询用户信息
//        SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
//        //账号锁定
        if(userTmp == null){
            throw new IncorrectCredentialsException("用户缓存未找到，请重新登陆");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userTmp, accessToken, getName());
        return info;
    }
}
