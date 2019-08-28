package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 17:23:11
 */
@Data
@TableName("wp_comm_pri")
public class WpCommPriEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer seqId;
	/**
	 *
	 */
	private Integer commId;
	/**
	 *
	 */
	private String commName;
	/**
	 *
	 */
	private String areaName;
	/**
	 *
	 */
	private Integer areaId;
	/**
	 *
	 */
	private String countryName;
	/**
	 *
	 */
	private Integer countryId;
	/**
	 *
	 */
	private String statFreq;
	/**
	 *
	 */
	private String currency;
	/**
	 *
	 */
	private String priType;
	/**
	 *
	 */
	private BigDecimal priToday;
	/**
	 *
	 */
	private String priUnit;
	/**
	 *
	 */
	private Date dataTime;
	/**
	 *
	 */
	private String dataSource;

}
