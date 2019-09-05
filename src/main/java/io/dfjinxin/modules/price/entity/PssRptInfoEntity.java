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
 * @date 2019-09-05 17:22:40
 */
@Data
@TableName("pss_rpt_info")
public class PssRptInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String rptId;
	/**
	 * 
	 */
	private String rptName;
	/**
	 * 
	 */
	private String commId;
	/**
	 * 
	 */
	private String areaCode;
	/**
	 * 
	 */
	private Date rptStartDate;
	/**
	 * 
	 */
	private Date rptEndDate;
	/**
	 * 
	 */
	private Date crteTime;
	/**
	 * 
	 */
	private String rvewName;
	/**
	 * 
	 */
	private Date rvewDate;
	/**
	 * 
	 */
	private String rvewRemarks;
	/**
	 * 
	 */
	private String rvewStatus;
	/**
	 * 
	 */
	private String rptCrtWay;
	/**
	 * 
	 */
	private String rptStatus;
	/**
	 * 
	 */
	private byte[] rptFile;
	/**
	 * 
	 */
	private Date delTime;

}
