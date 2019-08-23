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
 * @date 2019-08-23 15:38:58
 */
@Data
@TableName("pss_comm_total")
public class PssCommTotalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private String commId;
	/**
	 *
	 */
	private String commName;
	/**
	 *
	 */
	private String commAbb;
	/**
	 *
	 */
	private String parentCode;
	/**
	 *
	 */
	private Integer levelCode;
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private String createName;
	/**
	 *
	 */
	private Integer dataFlag;

}
