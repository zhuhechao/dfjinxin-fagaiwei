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
@TableName("pss_analy_relt")
public class PssAnalyReltEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer reltId;
	/**
	 * 
	 */
	private String analyId;
	/**
	 * 
	 */
	private Date analyTime;
	/**
	 * 
	 */
	private String reltName;
	/**
	 * 
	 */
	private String analyWay;
	/**
	 * 
	 */
	private String basVar;
	/**
	 * 
	 */
	private String tarVar;
	/**
	 * 
	 */
	private String analyCoe;
	/**
	 * 
	 */
	private String pvalue;

}
