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
import java.util.Date;

/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("pss_role_perm_info")
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private int permId;

	/**
	 * 角色ID
	 */
	private int roleId;

	/**
	 * 菜单ID
	 */
	private int menuId;

	/**
	 * 创建时间
	 */
	private Date creDate;

	/**
	 * 更新时间
	 */
	private Date updDate;
	
}
