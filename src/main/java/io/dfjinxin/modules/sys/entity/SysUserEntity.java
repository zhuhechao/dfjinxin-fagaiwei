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
import io.dfjinxin.common.validator.group.AddGroup;
import io.dfjinxin.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private  String userPass;

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
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;


	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer userStatus;



	/**
	 * 注册时间
	 */
	private Timestamp crteDate;

	/**
	 * 更新时间
	 */
	private Timestamp updDate;

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


}
