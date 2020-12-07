package io.dfjinxin.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.idsmanager.dingdang.jwt.DingdangUserRetriever;
import io.dfjinxin.common.utils.MD5Utils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.RedisUtil;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.modules.sys.entity.UrlEnum;
import io.dfjinxin.modules.sys.oauth2.OAuth2Token;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.jwtAuth.hmac.CryptoUtil;
import io.dfjinxin.modules.sys.jwtAuth.hmac.GenerateToken;
import io.dfjinxin.modules.sys.jwtAuth.jwt.SubjectInfo;
import io.dfjinxin.modules.sys.service.ShiroService;
import io.dfjinxin.modules.sys.service.SysRoleService;
import io.dfjinxin.modules.sys.service.SysUserService;
import io.dfjinxin.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("zhjg")
@Api(tags = "发改登录")
public class SmartPriceLoginController extends AbstractController {

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rsp-server.url}")
    private String rspUrl;

    @Value("${auth.client_id}")
    private String clientId;

    @Value("${auth.client_secret}")
    private String clientSecret;

    @Value("${auth.auth_url}")
    private String authUrl;
    @Value("${token.access-token.expiration-time}")
    private Integer accessTokenExpirationTime;
    @Value("${server.servlet.context-path}")
    private String applicationName;
    @Value("${token.refresh-token.expiration-time}")
    private Integer refreshTokenExpirationTime;

    @Value("${token.old-token.expiration-time}")
    private Integer oldTokenExpirationTime;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping(value = "/login")
    @ApiOperation("发改登陆接口")
    public R Login(@RequestParam(value = "id_token", required = false) String id_token, @PathVariable(value = "userName", required = false) String userName,@RequestParam(value = "userPass", required = false) String userPass) {
        SysUserEntity entity = new SysUserEntity();
        logger.info("Validate ca form data==> {}", id_token);
        String publicKey = "{\"kty\":\"RSA\",\"kid\":\"7835635588759989719\",\"alg\":\"RS256\",\"n\":\"nDSjqFpp1JOt15SW7r12kOY0ah5-yay_q9JTIqEfBYT4hzuUTegQVaNri7SfprmMG66K_PFCAn1Sei7CQx6Q4kuDVmUr2aFW2_LFSUvg-_hxOmOGrACJvuQ_s1ElNlYlfRGDcc9ZvHhlhE0QSHytOKekqUfJuXz-rwhuzMMBLD0NbOkzIG3zjUgbNI0rUz421fKExhV_Jm4OzM7BkOxZ1TQTP4zC28wbt8kOCoJPOsr8VPZBPe0z9wQKz5iy6WzG0quOQrGfiTvVIVFdgVyu4_r33XtTDUqGRdRrllLP8W7_40RN8tCoJ4mKhkvuJPt28b8Zny9rAI7BHw6uS0mDuw\",\"e\":\"AQAB\"}";
        //验证签名及解密
        DingdangUserRetriever retriever = new DingdangUserRetriever(id_token, publicKey);
        //用户信息
        DingdangUserRetriever.User user = null;
        //处理票据信息
        if (retriever != null) {
            logger.info("FG id_token informations :{}", retriever);
            //用户姓名
            try {
                user = retriever.retrieve();
            } catch (Exception e) {
                logger.error("发改认证中心用户信息获取失败:{}", e);
                return R.error("发改认证中心用户信息获取失败");
            }
            //判断自己系统中是否存在该用户
            String uid = user.getSub();
            System.out.println("获取发改用户信息"+uid+"+++++++++"+ user.getName()+"+++"+user.getUsername()+"++++++++");
            entity.setUserName(uid);
            entity.setUserPass("99ec41f1dc48f4c6a018b688411b456d");
            SysUserEntity sysUserEntity = sysUserService.queryByUserName(entity);
            if (sysUserEntity != null) {
             R res= loginValid(sysUserEntity);
                return res;
            } else {
                logger.error("智慧价格系统不存在该用户:{}", retriever);
                return R.error("智慧价格系统用户查找失败");
            }

        } else {
            logger.error("发改认证中心用户查找失败:{}", retriever);
            return R.error("发改认证中心用户查找失败");
        }
    }

    @GetMapping("/login/{userName}")
    @ApiOperation("发改登陆接口")
    public R Login( @PathVariable(value = "userName", required = false) String userName,@RequestParam(value = "userPass") String userPass) {
          if(userPass!=null && !userPass.equals("") ){
            SysUserEntity entity = new SysUserEntity();
            entity.setUserName(userName);
            entity.setUserPass(userPass);
            SysUserEntity sysUserEntity = sysUserService.queryByUserName(entity);
            if (sysUserEntity == null) {
                return R.error(1,"该用户不存在！");
            } else {
              return   loginValid(sysUserEntity);
            }
          }else {
              return R.error(1,"没有有效的密码信息！");
          }
    }

    //大屏展示--登录接口
    @GetMapping("/screeLogin")
    @ApiOperation("大屏登陆接口")
    public R largeScreenLogin(HttpServletRequest request) {
        String serverName = request.getServerName();
        if(serverName.equals(rspUrl)){
        SysUserEntity entity = new SysUserEntity();
        entity.setUserName("fgw");
        entity.setUserPass("99ec41f1dc48f4c6a018b688411b456d");
        SysUserEntity sysUserEntity = sysUserService.queryByUserName(entity);
        if (sysUserEntity != null) {
            R res= loginValid(sysUserEntity);
            R r = R.ok().put("token", res.get("token"));
            return r;
        } else {
            return R.error("智慧价格系统用户查找失败");
        }
        }else {
            logger.error("非法请求:{}", serverName);
            return R.error("ip认证失败");
        }
    }

    @Value("${ca.valid}")
    private boolean caVaid;

    @PostMapping("/logout")
    @ApiOperation("发改登出接口")
    public R logout() {
        String userId = ShiroUtils.getUserEntity().getUserId();
        SysUserTokenEntity entity = sysUserTokenService.lambdaQuery().eq(SysUserTokenEntity::getUserId, userId).one();
        redisUtil.deleteCache("AccessToken" + entity.getToken(),"RefreshToken" + entity.getToken());
        SecurityUtils.getSubject().logout();
        return R.ok();
    }

    /**
     * 获取accessToken,refreshToken
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/login/accessCheck")
    public R resAccessAudit(@RequestParam(value = "code", required = false) String code, @PathVariable(value = "state", required = false) String state){
        if(StringUtils.isNotBlank(code)){
            //获取token
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
            formData.add("client_id", clientId);
            formData.add("grant_type", "authorization_code");
            formData.add("code", code);
            formData.add("client_secret", clientSecret);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String codeBody = restTemplate.exchange(authUrl + UrlEnum.GET_TOKEN_URL.getUrl(), HttpMethod.POST,
                    new HttpEntity< MultiValueMap<String, String>>(formData,headers), String.class).getBody();
            JSONObject jsonObject = JSONObject.parseObject(codeBody);
            String accessToken = (String) jsonObject.get("access_token");
            String refreshToken = (String) jsonObject.get("refresh_token");
            String uid = (String) jsonObject.get("uid");
            //获取用户信息
            String userInfo= authUrl + UrlEnum.GET_USER_INFO.getUrl()+"?access_token="+accessToken+"&client_id="+clientId+"&uid="+uid;
            String userBody = restTemplate.exchange(userInfo, HttpMethod.GET,
                    new HttpEntity< MultiValueMap<String, String>>(null,headers), String.class).getBody();
            JSONObject userObject = JSONObject.parseObject(userBody);
            String loginName = (String) userObject.get("loginName");
            //根据用户Id获取用户信息
            SysUserEntity userById = sysUserService.getUserById(loginName);
            //获取菜单信息
            R res = loginValid(userById);
            if(StringUtils.isNotBlank(accessToken)){
                Map<String,Object> acMap = new HashMap();
                acMap.put("access_token",accessToken);
                acMap.put("token_time",new Date());
                redisUtil.hmset("AccessToken"+res.get("token"), acMap );
            }
            if(StringUtils.isNotBlank(refreshToken)){
                Map<String,Object> rfMap = new HashMap();
                rfMap.put("refresh_token",refreshToken);
                rfMap.put("token_time",new Date());
                redisUtil.hmset("RefreshToken"+res.get("token"), rfMap );
            }
            return res;

        }else {
            R res = new R();
            return res.error(401,"token 获取失败！");
        }
    }


    @GetMapping("/goToService")
    @ApiOperation("集成系统跳转")
    public R goToService(@RequestParam(value = "urlParm") String urlParm,HttpServletRequest request) throws IOException {
        GenerateToken generateToken = new GenerateToken();
        String user = request.getHeader("token");

       // 如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(user)){
            user = request.getParameter("token");
        }
       // String user= "79362e48e37283a7cdea0825e2614375";

       // SysUserTokenEntity tokenEntity = shiroService.queryByToken(user);
        String token = null;
        String sep = null;
        if (!Strings.isNullOrEmpty(urlParm)) {
            try {
                sep = (new URL(urlParm)).getQuery() == null ? "?" : "&";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            token =  MD5Utils.getMD5(user);
        }

        String url = urlParm.concat(sep + "very_token=" + token);
        return R.ok().put("url",url);

    }

//    @GetMapping(value = "/verifyToken")
//    @ApiOperation("集成系统认证")
//    public R sysVerify(@RequestParam(value = "very_token") String very_token) {
//        SubjectInfo subjectInfo = CryptoUtil.parseJwt(very_token);
//        String user = subjectInfo.getUserInfo();
//        return getAndCheckToken(user,0L,null);
//    }

    @GetMapping(value = "/verifyToken")
    @ApiOperation("子系统token是否过期验证")
    public R checkToken(@RequestParam Map<String,Object> params) {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();
        long st = localDateTime.toEpochSecond(zoneOffset);
         Map map = new HashMap();
         String nm = (String) params.get("nm");
         long tm =Long.parseLong((String) params.get("tm")) ;
         String clientId = (String) params.get("clientId");
         String token = (String) params.get("token");
         Cache.ValueWrapper cvalue= cacheManager.getCache("cacheToken").get(nm);
         if(cvalue !=null){
             return R.error(1,"不允许重复请求！");
         }else {
             cacheManager.getCache("cacheToken").put(nm,tm);
         }
         if(st-tm>90){
             return R.error(1,"请求时间超时");
         }
         if( clientId.equals("yuqing")){
           return getAndCheckToken(token,tm,"6AFjhK3MVNCyif");
         }else if(clientId.equals("diaoyan")){
             return getAndCheckToken(token,tm,"6BFmnvx7QERthz");
         }else {
             map.put("code",1);
         }

        return R.ok(map);
    }



    private R mockLogin(String mockUserId) {
        R createResult = sysUserTokenService.createToken(mockUserId);
        SysRoleEntity sysRoleEntity = sysRoleService.getById(mockUserId);
        String roleName = sysRoleEntity.getRoleName();
        createResult.put("roleName", roleName);
        SysUserEntity userEntity = sysUserService.getById(mockUserId);
        String userName = userEntity.getUserName();
        createResult.put("userName", userName);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(mockUserId);
        sysUserEntity.setUserName("mockUser");
        ShiroUtils.setSessionAttribute("user", sysUserEntity);
        return createResult;
    }


    private R getAndCheckToken(String token,long flag,String sert){
        List<SysUserTokenEntity> list=  shiroService.queryAllTokenUser();
        for(SysUserTokenEntity entity:list){
            String mdToken=MD5Utils.getMD5(entity.getToken());
            String tm = Long.toString(flag);
            String v = tm+mdToken+sert;
            String rToken = MD5Utils.getMD5(v);
            if(sert !=null && rToken.equals(token)){
                return  tokenTm(entity.getToken());
            }
        }

        return R.error(1,"token不合法！");
    }

    private  R tokenTm(String token){
        SysUserTokenEntity tokenEntity = shiroService.queryByToken(token);
        Map map = new HashMap();
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            return R.error(1,"会话超时！");
        }else {
            map.put("code", 0);
        }
        return R.ok(map);
    }

    private R loginValid(SysUserEntity sysUserEntity){
        List<Map<String,Object>> menus= sysUserService.getUserPerm(sysUserEntity.getUserId());
        R    createResult = sysUserTokenService.createToken(sysUserEntity.getUserId());
        Object token = createResult.get("token");
        Map<String,Object> map = new HashMap<>();
        map.put("userId",sysUserEntity.getUserId());
        map.put("userName",sysUserEntity.getUserName());
        map.put("userRealName",sysUserEntity.getUserRealName());
       SecurityUtils.getSubject().login(new OAuth2Token(String.valueOf(token), sysUserEntity));
        createResult.put("menu",menus);
        createResult.put("userInfo",map);
        return createResult;
    }

}
