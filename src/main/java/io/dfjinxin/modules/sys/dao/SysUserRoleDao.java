/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {


	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Integer> queryRoleIdList(String userId);


	/**
	 * 根据用户ID 删除用户的角色关系
	 */
	void deleteUerRole(String userId);

	/**
	 * 根据角色ID数组，获取对应用户角色信息
	 */
	List<SysUserRoleEntity> listByIds(List<Integer> roles);
}
