/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.MapUtils;
import io.dfjinxin.modules.sys.dao.SysUserRoleDao;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserRoleEntity;
import io.dfjinxin.modules.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserRoleService")
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {
    @Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public void saveOrUpdate(String userId, List<Integer> roleIdList) {
		//先删除用户与角色关系
		this.removeByMap(new MapUtils().put("user_id", userId));

       if(roleIdList.size()>0) {
		   //保存用户与角色关系
		   for (int roleId : roleIdList) {
			   SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
			   sysUserRoleEntity.setUserId(userId);
			   sysUserRoleEntity.setRoleId(roleId);

			   this.save(sysUserRoleEntity);
		   }
	   }
	}


	@Override
	public void deleteUserRole(String userId) {
		sysUserRoleDao.deleteUerRole(userId);
	}

	@Override
	public List<SysUserRoleEntity> listByIds(List<Integer> roleid) {
		return baseMapper.listByIds(roleid);
	}


}
