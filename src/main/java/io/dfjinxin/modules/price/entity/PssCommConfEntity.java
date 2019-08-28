package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 19:33:54
 */
@Data
@TableName("pss_comm_conf")
public class PssCommConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer commId;
	/**
	 * 
	 */
	private Integer ewarnId;
	/**
	 * 
	 */
	private Integer delFlag;
	/**
	 * 
	 */
	private Date crteDate;
	/**
	 * 
	 */
	private String remarks;

}
