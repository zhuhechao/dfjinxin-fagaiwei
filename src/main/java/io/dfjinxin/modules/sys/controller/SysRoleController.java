/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.service.SysRoleMenuService;
import io.dfjinxin.modules.sys.service.SysRoleService;
import io.dfjinxin.common.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 所有部门列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		PageUtils page = sysRoleService.queryPage(params);
		List<SysRoleEntity> list = (List<SysRoleEntity>)page.getList();
		page.setList(list);

		return R.ok().put("page", page);
	}




	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody int[] roleIds){
		sysRoleService.deleteBatch(roleIds);

		return R.ok();
	}

	/**
	 * 获取角色权限
	 */
	@SysLog("获取角色的权限信息")
	@GetMapping("/rolePerm")
	@RequiresPermissions("sys:role:rolePerm")
	public List<Map<String,Object>> getRolePerm(String roleId){
	List<Map<String,Object>> perm=	sysRoleService.rolePerm(roleId);
	return perm;
	}

	/**
	 * 保存或更新
	 */
	@SysLog("获取角色的权限信息")
	@PostMapping("/info")
	@RequiresPermissions("sys:role:info")
	public void addOrUpdate(@RequestBody SysRoleEntity role){
		sysRoleService.addOrUpdate(role);
	}

}
