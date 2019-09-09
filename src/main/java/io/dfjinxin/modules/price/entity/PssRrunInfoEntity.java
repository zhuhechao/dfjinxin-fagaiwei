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
 * @date 2019-09-03 10:02:41
 */
@Data
@TableName("pss_rrun_info")
public class PssRrunInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer rptId;
	/**
	 * 
	 */
	private String rptName;
	/**
	 * 
	 */
	private Date statTime;
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
	private Date runStartTime;
	/**
	 * 
	 */
	private Date runEndTime;
	/**
	 * 
	 */
	private String runStatus;
	/**
	 * 
	 */
	private String runDetail;

}
