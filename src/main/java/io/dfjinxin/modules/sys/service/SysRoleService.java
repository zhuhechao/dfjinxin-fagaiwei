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
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);


	R addOrUpdate(SysRoleEntity role);

	R deleteBatch(ArrayList<Integer> roleIds);

	//角色功能权限
	public R rolePerm(String roleId);

	//新增角色
	void addRole(SysRoleEntity role);

	//获取角色下拉框数据
	List<Map<String,Object>> getRole();

	//角色赋权验证
	R checkPerm(SysRoleEntity role);

	//删除角色时校验
	R checkPermInfo(ArrayList<Integer> roles);

	SysRoleEntity getRoleById(String roleId);


}
