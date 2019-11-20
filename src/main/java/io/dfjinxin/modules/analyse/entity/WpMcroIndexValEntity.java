package io.dfjinxin.modules.analyse.entity;

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
 * @date 2019-09-09 11:23:36
 */
@Data
@TableName("wp_macro_index_val")
public class WpMcroIndexValEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer seqId;
	/**
	 *
	 */
	private Integer indexId;
	/**
	 *
	 */
	private String indexName;
	/**
	 *
	 */
	private Integer indexType;
	/**
	 *
	 */
	private Integer statAreaId;
	/**
	 *
	 */
	private String statAreaName;
	/**
	 *
	 */
	private Integer statFrequency;
	/**
	 *
	 */
	private BigDecimal indexVal;
	/**
	 *
	 */
	private String indexUnit;
	/**
	 *
	 */
	private Date dataTime;
	/**
	 *
	 */
	private String dataSource;

}
