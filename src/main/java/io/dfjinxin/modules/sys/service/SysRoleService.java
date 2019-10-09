/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);


	void addOrUpdate(SysRoleEntity role);

	void deleteBatch(int[] roleIds);

	//角色功能权限
	public List<Map<String,Object>> rolePerm(String roleId);

	//新增角色
	void addRole(SysRoleEntity role);

	//获取角色下拉框数据
	List<Map<String,Object>> getRole();


}
