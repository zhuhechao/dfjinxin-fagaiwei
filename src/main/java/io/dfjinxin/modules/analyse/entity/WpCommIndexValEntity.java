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
 * @date 2019-09-02 15:38:19
 */
@Data
@TableName("wp_comm_index_val")
public class WpCommIndexValEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer indexI;
	/**
	 *
	 */
	private Integer commId;
	/**
	 *
	 */
	private String indexName;
	/**
	 *
	 */
	private String indexType;
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
	private String statFrequency;
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
