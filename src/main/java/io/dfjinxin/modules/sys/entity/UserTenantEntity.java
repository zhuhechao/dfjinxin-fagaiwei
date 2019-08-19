package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户租户信息表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-12 17:06:08
 */
@Data
@TableName("user_tenant")
public class UserTenantEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 租户名称
	 */
	private String tenantName;
	/**
	 * 租户详情
	 */
	private String tenantDesc;
	/**
	 * 状态 0:启用 1:未启用
	 */
	private Integer status;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 管理员电话
	 */
	private String managerPhone;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
