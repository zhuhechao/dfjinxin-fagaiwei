/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.exception.RRException;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.entity.GovRootMenuEntity;
import io.dfjinxin.modules.sys.entity.MenuParams;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.service.ShiroService;
import io.dfjinxin.modules.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import io.dfjinxin.common.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 系统菜单
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "菜单管理")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(1);
		Set<String> permissions = shiroService.getUserPermissions("1");
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysMenuService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0);
		root.setMenuName("一级菜单");
		root.setPareMenuId(-1);
		root.setMenuState(1);
		menuList.add(root);

		return R.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 */
	@ApiOperation("获取指定菜单信息")
	@GetMapping("/info")
	@RequiresPermissions("sys:menu:info")
	public R info(@RequestParam("menuId") String menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存或修改菜单
	 */
	@ApiOperation("保存菜单")
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		  R r= sysMenuService.checkMenuInfo(menu);
		  Integer ft = (Integer) r.get("code");
		  String msg = (String) r.get("msg");
		  if(ft == 1){
		  	return  R.error(1,msg);
		  }

        if( menu.getMenuId() ==0){
			sysMenuService.save(menu);
		}else {
			sysMenuService.updateById(menu);
		}

		return R.ok();
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	@ApiOperation("删除菜单")
	public R delete(@RequestBody MenuParams mid){
     List<Integer> mids=   mid.getIds();
		//判断是否有子菜单或按钮
		for(Integer d : mids){
			List<SysMenuEntity> menuList = sysMenuService.queryListParentId(d);
			if(menuList.size() > 0){
				return  R.error(1,"需要删除的目录下包含子菜单");
			}
			sysMenuService.delete(d);
		}

		return R.ok();
	}

	/**
	 * 菜单下拉框
	 */
	@GetMapping("/userMenuInfo")
	public R userMenuInfo( ){
		List<Map<String,Object>> menu = sysMenuService.serMenuInfo();
		return R.ok().put("menu", menu);
	}



}
