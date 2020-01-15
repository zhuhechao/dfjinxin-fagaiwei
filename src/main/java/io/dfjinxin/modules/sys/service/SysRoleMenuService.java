/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.entity.SysRoleMenuEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {
	
	R saveOrUpdate(int roleId, int roleTypeId, List<Integer> menuIdList);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Map<String,Object>> queryMenuList(Map<String,Object> roleId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(ArrayList<Integer> roleIds);

	/**
	 * 根据查询菜单信息
	 */
	List<Map<String,Object>> select(Map<String,Object> map);
	
}
