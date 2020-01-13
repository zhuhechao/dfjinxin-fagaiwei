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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.dfjinxin.common.validator.group.AddGroup;
import io.dfjinxin.common.validator.group.UpdateGroup;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("pss_user_info")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 用户ID
	 */
	@TableId
	private String userId;

	private String userName;
	/**
	 * 用户密码
	 */
	private  String userPass ;

	/**
	 * 用户真实名称
	 */
	private String userRealName;

	/**
	 * 手机号
	 */
	private String mblPhoneNo;

	/**
	 * 座机号
	 */
	private String fixTphoneNo;

	/**
	 * 盐
	 */
	@TableField(exist = false)
	private String salt;

	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",message = "不满足邮箱正则表达式")
	private String email;


	/**
	 * 状态  false：禁用   true：正常
	 */
	private int userStatus;



	/**
	 * 注册时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date crteDate;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updDate;

	/**
	 * 用户和角色关系
	 *
	 */
	@TableField(exist = false)
	private ArrayList<Integer> roles;

	/**
	 * 用户和部门
	 *
	 */
    @TableField(exist = false)
	private  String depId;

	/**
	 * 角色类型
	 */
	@TableField(exist = false)
    private int  roleTypeId;

	/**
	 * 前端角色类型判断标识
	 */
	@TableField(exist = false)
	private Boolean status;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String depName;

	/**
	 * 角色名称
	 */
	@TableField(exist = false)
	private String roleName;

	/**
	 * 角色名称
	 */
	@TableField(exist = false)
	private String roleId;

	@TableField(exist = false)
	private int error_no;

}
