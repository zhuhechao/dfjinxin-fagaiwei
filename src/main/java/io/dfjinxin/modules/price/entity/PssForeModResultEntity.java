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
 * @date 2019-09-04 09:33:51
 */
@Data
@TableName("pss_fore_mod_result")
public class PssForeModResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String modId;
	/**
	 * 
	 */
	private String modName;
	/**
	 * 
	 */
	private String commId;
	/**
	 * 
	 */
	private String modType;
	/**
	 * 
	 */
	private String dataSetId;
	/**
	 * 
	 */
	private String algoId;
	/**
	 * 
	 */
	private String algoName;
	/**
	 * 
	 */
	private String indeVar;
	/**
	 * 
	 */
	private String depeVar;
	/**
	 * 
	 */
	private BigDecimal mape;
	/**
	 * 
	 */
	private String modDetail;

}
