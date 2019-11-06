package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
	private Integer commId;
	/**
	 *
	 */
	private String commName;
	/**
	 *
	 */
//	private String commAbb;
	/**
	 *
	 */
	private Integer parentCode;
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
	private Integer dataFlag;

	@TableField(exist = false)
	private String remarks;

	@TableField(exist = false)
	private String ewarnId;

	@TableField(exist = false)
	private String ewarnName;

	@TableField(exist = false)
	private Integer ewarnTypeId;

	@TableField(exist = false)
	private Integer indexId;

	/**
	 * pss_comm_conf 表主键
	 */
	@TableField(exist = false)
	private Integer confId;

	@TableField(exist = false)
	private List<PssCommTotalEntity> subCommList;

}
