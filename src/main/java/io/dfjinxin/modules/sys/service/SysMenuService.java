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
import io.dfjinxin.modules.sys.entity.GovRootMenuEntity;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysMenuService extends IService<SysMenuEntity> {
	/**
	 * 查询菜单信息
	 */
	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(int parentId, List<Integer> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(int parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();


	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(int userId);

	/**
	 * 删除
	 */
	void delete(int menuId);

	/**
	 * 菜单下拉框
	 */
	List<Map<String,Object>> serMenuInfo();

	/**
	 * 获取指定菜单的用户信息
	 * @param menuId
	 * @return
	 */
	SysMenuEntity getById(int menuId);

	R checkMenuInfo(SysMenuEntity sysMenuEntity);

}
