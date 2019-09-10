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
 * @date 2019-09-10 09:22:42
 */
@Data
@TableName("pss_rsch_conf")
public class PssRschConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String rschId;
	/**
	 * 
	 */
	private String rschName;
	/**
	 * 
	 */
	private String rschType;
	/**
	 * 
	 */
	private String rschFreq;
	/**
	 * 
	 */
	private String execType;
	/**
	 * 
	 */
	private Date execTime;
	/**
	 * 
	 */
	private Date endTime;
	/**
	 * 
	 */
	private String execCdt;
	/**
	 * 
	 */
	private String rschRemarks;
	/**
	 * 
	 */
	private Date crteTime;

}
