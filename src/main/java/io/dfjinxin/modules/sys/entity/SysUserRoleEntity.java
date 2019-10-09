/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户与角色对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("pss_user_role_rela")
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private int relId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 角色ID
	 */
	private int roleId;

	/**
	 * 创建时间
	 */
	private Timestamp creDate;

	/**
	 * 更新时间
	 */
	private Timestamp updDate;

	
}
