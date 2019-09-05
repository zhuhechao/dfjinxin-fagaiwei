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
 * @date 2019-09-05 17:13:08
 */
@Data
@TableName("pss_rpt_conf")
public class PssRptConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String commId;
	/**
	 * 
	 */
	private String rschId;
	/**
	 * 
	 */
	private String rptId;
	/**
	 * 
	 */
	private String rptName;
	/**
	 * 
	 */
	private Date crteDate;
	/**
	 * 
	 */
	private String crteName;
	/**
	 * 
	 */
	private String rptRemarks;
	/**
	 * 
	 */
	private String rvewName;
	/**
	 * 
	 */
	private String rvewRemarks;
	/**
	 * 
	 */
	private String rptStatus;

}
