package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("wp_mcro_index_info")
public class WpMcroIndexInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
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
	private Integer indexFlag;
	/**
	 * 
	 */
	private String indexDescribe;
	/**
	 * 
	 */
	private Date dataTime;

}
