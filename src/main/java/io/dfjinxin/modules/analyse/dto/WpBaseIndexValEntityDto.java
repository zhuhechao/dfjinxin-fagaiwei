package io.dfjinxin.modules.analyse.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@Data
@Accessors(chain = true)
public class WpBaseIndexValEntityDto implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private String indexName;
	/**
	 *
	 */
	private String indexType;
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
	private String unit;
	/**
	 *
	 */
	private String sourceName;
	/**
	 *
	 */
	private Double value;
	/**
	 *
	 */
	private Date date;

	private String commName;

}
