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
import io.dfjinxin.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 查询用户数据
	 */
	PageUtils queryUserList(Map<String, Object> params);

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
	 * 根据用户信息，查询系统用户
	 */
	SysUserEntity queryByUserName(SysUserEntity sysUserEntity);

	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user);

	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 批量删除用户
	 */
	void deleteBatch(String[] userIds);

	/**
	 * 单条删除
	 */
    void deleteUser(String userId);

	/**
	 * 根据ID 查询用户信息
	 *
	 */
	SysUserEntity getUserById(String userId);

	/**
	 * 根据用户Id 获取可访问的菜单
	 */
	List<Map<String,Object>> getUserPerm(String userId);

}
