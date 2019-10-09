/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserRoleEntity;

import java.util.List;



/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

	void saveOrUpdate(String userId, List<Integer> roleIdList);

	/**
	 * 根据用户角色
	 */
	void deleteUserRole(String userId);

	/**
	 * 查询角色和用户
	 */
	List<SysUserRoleEntity> listByIds(List<Integer> roles);
}
