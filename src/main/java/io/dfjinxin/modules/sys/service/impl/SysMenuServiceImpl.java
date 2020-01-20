/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.ClassPath;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.MapUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.dao.SysMenuDao;
import io.dfjinxin.modules.sys.entity.GovRootMenuEntity;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.service.SysMenuService;
import io.dfjinxin.modules.sys.service.SysRoleMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("sysMenuService")
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Override
	public List<SysMenuEntity> queryListParentId(int parentId, List<Integer> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(int parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}



	@Override
	public List<SysMenuEntity> getUserMenuList(int userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Integer> menuIdList = baseMapper.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(int menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}

	@Override
	public List<Map<String, Object>> serMenuInfo() {
		return baseMapper.searMenu();
	}

	@Override
	public SysMenuEntity getById(int menuId) {
		return sysMenuDao.getMenuById(menuId);
	}


	/**
	 * 验证参数是否正确
	 */
	@Override
	public R checkMenuInfo(SysMenuEntity menu) {
			String menuName = menu.getMenuName();
			int menuId = menu.getMenuId();
			if(menuName !=null && !menuName.equals("")){
				Map<String,Object> map = new HashMap<>();
				map.put("menu_name",menuName);
				List<SysMenuEntity> re =  baseMapper.selectByMap(map);
				int sid = 0;
//				if(re.size()>0){
//					sid = re.get(0).getMenuId();
//				}
//				if(menuId == 0 && re.size()>0){
//					return R.error(1,"菜单名称重复");
//				}else if(menuId != 0 && sid!=0 &&  sid != menuId){
//					return R.error(1,"菜单名称已存在！");
//				}
			}else {
				return R.error(1,"菜单名称不能为空");
			}

			if(menu.getMenuId()!=1 && menu.getPareMenuId() ==0){
				return R.error(1,"上级菜单不能为空");
			}

			//菜单
			if(menu.getMenuType() != Constant.MenuType.BUTTON.getValue()){
				if(StringUtils.isBlank(menu.getMenuRouter())){
					return R.error(1,"菜单路由不能为空");
				}
			}

			//上级菜单类型
			int parentType = Constant.MenuType.CATALOG.getValue();
			if(menu.getPareMenuId() != 0){
				SysMenuEntity parentMenu = sysMenuDao.getMenuById(menu.getPareMenuId());
				parentType = parentMenu.getMenuType();
			}

			//目录、菜单
			if(menu.getMenuType() == Constant.MenuType.CATALOG.getValue() ||
					menu.getMenuType() == Constant.MenuType.MENU.getValue()){
				if(parentType != Constant.MenuType.CATALOG.getValue()){
					return R.error(1,"上级菜单只能为目录类型");
				}
				return R.ok();
			}

			return R.ok();
		}


	public List<Integer> queryAllMenuId(int userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Integer> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Integer> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getMenuType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		long no = params.containsKey("pageIndex") ? Long.valueOf(params.get("pageIndex").toString()) : 1;
		long limit = params.containsKey("pageSize") ? Long.valueOf(params.get("pageSize").toString()) : 10;
		IPage<SysMenuEntity> page = baseMapper.queryMenu(new Page<>(no, limit), params);
		List<SysMenuEntity> list = page.getRecords();
		for(SysMenuEntity map:list){
			Integer st= map.getMenuState();
			if(st == 1){
				map.setStatus(true);
			}else {
				map.setStatus(false);
			}
		}

		return new PageUtils(page);
	}
}
