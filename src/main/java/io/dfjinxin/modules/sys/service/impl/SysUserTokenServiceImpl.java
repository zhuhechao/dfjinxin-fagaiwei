/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.dao.SysUserTokenDao;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.oauth2.TokenGenerator;
import io.dfjinxin.modules.sys.service.SysUserTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


@Service("sysUserTokenService")
@Transactional(rollbackFor = Exception.class)
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;


	@Override
	public R createToken(String userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		Timestamp tn = new Timestamp(now.getTime());
		//过期时间
		Timestamp expireTime = new Timestamp(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = this.getById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(tn);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			this.save(tokenEntity);
		}else{
			Calendar expireTimeCal = Calendar.getInstance();
			expireTimeCal.setTime(tokenEntity.getExpireTime());

			Calendar currTimeCal = Calendar.getInstance();
			currTimeCal.setTime(now);

			if(expireTimeCal.before(currTimeCal)){
				tokenEntity.setUpdateTime(now);
				tokenEntity.setExpireTime(expireTime);
				this.updateById(tokenEntity);
			}

			token = tokenEntity.getToken();
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(String userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		this.updateById(tokenEntity);
	}
}
