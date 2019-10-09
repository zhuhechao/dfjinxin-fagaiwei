/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {
	
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
	 * 查询用户的所有菜单ID
	 */
	List<Integer> queryAllMenuId(int userId);

	/**
	 * 查询菜单信息--分页
	 */
	IPage<SysMenuEntity> queryMenu(Page page, @Param("m") Map<String, Object> m);

	/**
	 * 菜单信息下拉框
	 */
	List<Map<String,Object>> searMenu();

	/**
	 * 获取指定Ip的菜单信息
	 */
	SysMenuEntity getMenuById(int menuId);


}
