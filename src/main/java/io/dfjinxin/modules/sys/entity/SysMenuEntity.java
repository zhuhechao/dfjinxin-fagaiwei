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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("pss_menu_info")
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@TableId
	private int menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private int pareMenuId;

	/**
	 * 父级菜单名称
	 */
	@TableField(exist = false)
	private String pareMenuName;


	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String menuPerm;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer menuType;

	/**
	 * 菜单URL
	 */
	private String  menuUrl;


	/**
	 * 菜单图标
	 */
	private String menuIconUrl;

	/**
	 * 创建时间
	 */
	private Date creDate;

	/**
	 * 更新时间
	 */
	private Date  updDate;

	/**
	 * 菜单状态
	 */
	private int menuState;

	/**
	 * 菜单路由
	 */
    private String menuRouter;

	/**
	 * 菜单展示排序
	 */
	private int menuOrder;

	@TableField(exist = false)
	private List<SysMenuEntity> list;

	/**
	 * 状态
	 */
	@TableField(exist = false)
	private boolean status;

	/**
	 * 菜单类型名称
	 */
	@TableField(exist = false)
	private String menuTypeName;


}
