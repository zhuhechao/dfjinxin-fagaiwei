package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@Data
@TableName("pss_dataset_info")
public class PssDatasetInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String dataSetId;
	/**
	 * 
	 */
	private String dataSetName;
	/**
	 * 
	 */
	private String dataSetType;
	/**
	 * 
	 */
	private String realSql;
	/**
	 * 
	 */
	private Date crteTime;

}
