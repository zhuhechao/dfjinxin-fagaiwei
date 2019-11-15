package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@Data
@TableName("wp_macro_index_info")
public class WpMcroIndexInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer indexId;
	private String unit;
	private String indexName;
	private String areaName;
	private String frequence;
	private String sourceName;
	private String indexType;
	private Integer indexFlag;

	/**
	 *
	 */
	@TableField(exist = false)
	private String indexDescribe;
	//码表中对应的名称
	@TableField(exist = false)
	private String codeName;
	@TableField(exist = false)
	private Date dataTime;

	@TableField(exist = false)
	private List subList;
}
