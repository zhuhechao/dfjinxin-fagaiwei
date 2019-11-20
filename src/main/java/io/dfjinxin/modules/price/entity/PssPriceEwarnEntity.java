package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2019-08-26 17:21:32
 */
@Data
@TableName("pss_price_ewarn")
public class PssPriceEwarnEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer ewarnId;
	/**
	 *
	 */
	private Integer commId;
	/**
	 *
	 */
	private Date ewarnDate;
	/**
	 *
	 */
	private String statAreaCode;
	/**
	 *
	 */
	private Integer pricTypeId;
	/**
	 *
	 */
	private String ewarnTypeId;

	private String unit;
	/**
	 *
	 */
	private String ewarnLevel;
	/**
	 *
	 */
	private BigDecimal priValue;
	/**
	 *
	 */
	private BigDecimal priRange;

	@TableField(exist = false)
	private String commName;

}
