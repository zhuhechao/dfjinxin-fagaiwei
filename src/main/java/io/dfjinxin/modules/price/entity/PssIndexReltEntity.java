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
 * @date 2019-09-04 17:32:05
 */
@Data
@TableName("pss_index_relt")
public class PssIndexReltEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String indexId;
	/**
	 * 
	 */
	private String modId;
	/**
	 * 
	 */
	private String indexName;
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
	private BigDecimal foreIndex;
	/**
	 * 
	 */
	private Date foreTime;

}
