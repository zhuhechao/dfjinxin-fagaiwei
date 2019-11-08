/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("pss_user_role_info")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@TableId
	private int roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	private String remarks;
	
	/**
	 * 角色状态
	 */
	private int roleState;

	/**
	 * 角色类型
	 */
	private int roleTypeId;

	
	/**
	 * 创建时间
	 */
	private Timestamp creDate;

	/**
	 * 更新时间
	 */
	private Timestamp updDate;

	@TableField(exist = false)
	private boolean status;

	@TableField(exist = false)
	private String roleTypeName;

	@TableField(exist = false)
	List<Integer> menuIdList;

	@TableField(exist = false)
	String menuIds;

	@TableField(exist = false)
	int error_no;
}
