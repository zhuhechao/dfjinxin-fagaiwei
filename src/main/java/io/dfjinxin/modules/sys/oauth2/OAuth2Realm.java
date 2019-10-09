/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.oauth2;

import com.google.gson.Gson;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
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
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
        //token失效
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token失效，请重新登录");
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
