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
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Integer> queryAllMenuId(String userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(SysUserEntity sysUserEntity);

	/**
	 * 根据用户名删除用户
	 */
	int deleteByUserName(String username);

	/**
	 * 查询用户信息
	 */
	IPage<SysUserEntity> queryUserList(Page page, @Param("m") Map<String, Object> m);

	/**
	 * 查询指定用户信息
	 * @param m
	 * @return
	 */
	SysUserEntity queryUserList(@Param("m") Map<String,Object> m);

	//新增用户
	int insertUserData(SysUserEntity sysUserEntity);

	//更新用户
	int updateUserData(SysUserEntity sysUserEntity);

	//根据ID查询用户信息
	SysUserEntity queryByUserId(@Param(value = "userId") String userId);

	//获取指定用户可访问的资源
    List<Map<String,Object>> getUserPerm(@Param(value = "userId") String userId);

}
