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
 * @date 2019-09-02 15:38:20
 */
@Data
@TableName("wp_base_index_info")
public class WpBaseIndexInfoEntity implements Serializable {
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
	private String indexFlag;
	/**
	 *
	 */
	private String indexDescribe;
	/**
	 *
	 */
	private Date dataTime;

}
