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
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

	/**
	 * 获取部门信息--分页
	 */
	IPage<SysRoleEntity> queryRole(Page page, @Param("m") Map<String, Object> m);

	/**
	 * 获取指定角色信息
	 */
	SysRoleEntity queryRole(@Param("m") Map<String, Object> m);
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<SysRoleEntity> queryRoleIdList(String createUserId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(int[] roleIds);

	/**
	 * 获取角色下拉框信息
	 */
	List<Map<String,Object>> getRole();

	/**
	 * 角色信息新增
	 *
	 */
	int save(SysRoleEntity role);

	/**
	 * 角色信息保存
	 *
	 */
	int updateRole(SysRoleEntity role);


}
