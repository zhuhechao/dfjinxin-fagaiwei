package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@TableName("pss_price_relt")
public class PssPriceReltEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String commId;
	/**
	 * 
	 */
	private String modId;
	/**
	 * 
	 */
	private String dataSetId;
	/**
	 * 
	 */
	private Date dataDate;
	/**
	 * 
	 */
	private String modType;
	/**
	 * 
	 */
	private BigDecimal forePrice;
	/**
	 * 
	 */
	private Date foreTime;
	/**
	 * 
	 */
	private BigDecimal reviPrice;
	/**
	 * 
	 */
	private Date reviTime;

}
