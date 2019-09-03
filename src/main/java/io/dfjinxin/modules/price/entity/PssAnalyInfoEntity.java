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
 * @date 2019-09-02 17:03:24
 */
@Data
@TableName("pss_analy_info")
public class PssAnalyInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer analyId;
	/**
	 * 
	 */
	private String analyName;
	/**
	 * 
	 */
	private String analyWay;
	/**
	 * 
	 */
	private Integer dataSetId;
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
	private String remarks;
	/**
	 * 
	 */
	private Date crteTime;

}
