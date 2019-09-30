package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-14 16:04:09
 */
@Data
@TableName("pss_rsch_conf")
public class PssRschConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	@TableId
	private Long rschId;
	/**
	 * 调度名称
	 */
	private String rschName;
	/**
	 * 调度类型:模型调度：01,报告调度：02，预警调度：03
	 */
	private String rschType;
	/**
	 * 调度频度  日频 :D, 周：W,旬：TD， 月频:M,  季频:Q,  年频:Y，指定日期:SP
	 */
	private String rschFreq;
	/**
	 * 执行时间
	 */
	private Date execTime;
	/**
	 * 执行方式    执行一次：1  重复执行：2
	 */
	private String execType;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 执行条件   始终执行:01   握手协议:02
	 */
	private String execCdt;
	/**
	 * spring bean名称
	 */
	private String beanName;

	/**
	 * 删除标记  0：正常  1：删除
	 */
	private String del_flag;
	/**
	 * 调度说明
	 */
	private String rschRemark;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
