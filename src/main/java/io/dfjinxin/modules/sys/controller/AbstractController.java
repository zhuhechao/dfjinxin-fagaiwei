/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.exception.LoginException;
import io.dfjinxin.common.utils.HttpContextUtils;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller公共组件
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${ca.valid}")
	private boolean caVaid;

	protected String getUserId() {
		if(caVaid){
			SysUserEntity userEntityFromShiro = ShiroUtils.getUserEntity();
			if(userEntityFromShiro==null){
				throw new LoginException("用户id为空，请重新登录");
			}
			String userId = userEntityFromShiro.getUserId();
			if(userId==null){
				throw new LoginException("用户id为空，请重新登录");
			}
			return userId;
		}else{
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			String UserId = (String)request.getSession().getAttribute("UserId");
			System.out.println("当前用户UserId: " + UserId);
			if(StringUtils.isBlank(UserId)){
				throw new LoginException("用户id为空，请重新登录");
			}
			return UserId;
		}
	}
	protected Long getTenantId(){
		if(caVaid){
			SysUserEntity userEntityFromShiro = ShiroUtils.getUserEntity();
			Long TenementId = userEntityFromShiro.getTenantId();
			logger.info("当前用户TenementId:{}",TenementId);
			if(TenementId==null){
				throw new LoginException("租户id为空，请重新登录");
			}
			return TenementId;
		}else{
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			String TenementId = (String)request.getSession().getAttribute("TenementId");
			System.out.println("当前用户TenementId: " + TenementId);
			if(StringUtils.isBlank(TenementId)){
				throw new LoginException("租户id为空，请重新登录");
			}
			return Long.parseLong(TenementId);
		}
	}

	/**
	 * 添加租户，用户信息用于过滤
	 * @param params
	 */
	protected void appendAuthFilter(Map<String, Object> params){
		//租户
		params.put("tenantId", getTenantId());
		params.put("userId", getUserId());
	}

}
