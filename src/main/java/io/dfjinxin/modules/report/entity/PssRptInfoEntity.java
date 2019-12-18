package io.dfjinxin.modules.report.entity;

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
 * @date 2019-09-20 11:07:08
 */
@Data
@TableName("pss_rpt_info")
public class PssRptInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long rptId;
	/**
	 * 报告名称
	 */
	private String rptName;
	/**
	 * 商品id
	 */
	private Integer commId;
	/**
	 * 商品名称
	 */
	private String commName;
	/**
	 * 报告频度 D:日    W:周    TD:旬    M:月    Q:季    Y:年    
	 */
	private String rptFreq;
	/**
	 * 报告类型 0 自动 1 手工 
	 */
	private String rptType;
	/**
	 * 统计区域代码
	 */
	private String areaCode;
	/**
	 * 生成时间
	 */
	private Date crteTime;
	/**
	 * 报告路径
	 */
	private String rptPath;
	/**
	 * 报告状态 1.有效  0.删除
	 */
	private String rptStatus;
	/**
	 * 报告文件名称
	 */
	private String rptFile;
	/**
	 * 删除时间
	 */
	private Date delTime;

}
