package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *modify 2019.11.11
 *表结构修改
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:20
 */
@Data
@TableName("wp_base_index_info")
public class WpBaseIndexInfoEntity implements Serializable {
	private static final long serialVersionUID = 1905122041950251207L;

	/**
	 *
	 */
	@TableId
	private Integer indexId;
	/**
	 *
	 */
	private Integer commId;
	/**
	 *
	 */
	private String indexType;
	/**
	 *
	 */
	private String indexName;
	/**
	 *
	 */
	private String indexUsed;
	/**
	 *
	 */
	private String unit;
	/**
	 *
	 */
	private String areaName;
	/**
	 *
	 */
	private String frequence;
	/**
	 *
	 */
	private String sourceName;
	/**
	 *
	 */
	private Integer indexFlag;

	@TableField(exist = false)
	private Date dataTime;

	@TableField(exist = false)
	private List subList;

}
