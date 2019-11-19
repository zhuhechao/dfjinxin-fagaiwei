/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Map<String,Object>> queryMenuList(@Param(value = "m") Map<String,Object> roleId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(ArrayList<Integer> roleIds);

	/**
	 * 查询菜单信息
	 */
	List<Map<String,Object>> select(@Param(value = "m") Map<String,Object> m);
}
