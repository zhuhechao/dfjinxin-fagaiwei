package io.dfjinxin.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.dfjinxin.modules.report.dto.PssRptConfDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 
 * 
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-18 11:39:58
 */
@Data
@TableName("pss_rpt_conf")
public class PssRptConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long rptId;
	/**
	 * 商品ID
	 */
	private Integer commId;
	/**
	 * 报告类型 0 自动 1 手工
	 */
	private String rptType;
	/**
	 * 报告频度 D:日  W:周  TD:旬  M:月  Q:季  Y:年
	 */
	private String rptFreq;
	/**
	 * 调度配置id 类型为自动  时有值 手动时为空
	 */
	private Integer rschId;
	/**
	 * 报告日期
	 */
	private Date rptDate;
	/**
	 * 报告名称
	 */
	private String rptName;

	//商品名称
	private String commName;

	//模板id
	private Integer tempId;


	/**
	 * 统计区域代码
	 */
	private String statCode;
	/**
	 * 模板路径
	 */
	private String rptPath;
	/**
	 * 生成时间
	 */
	private Date crteDate;
	/**
	 * 创建者
	 */
	private String crteName;
	/**
	 * 报告状态 0 正常 1 删除
	 */
	private String rptStatus;
	/**
	 * 报告备注
	 */
	private String rvewRemarks;
	/**
	 * 附件路径  人工报告使用
	 */
	private String rptAttachmentPath;
	/**
	 * 人工报告 0不审核，1提交审核
	 */
	private String revStatus;

	/**
	 * 调度模板名称 :自动报告使用
	 */
	private String rptTemplateName;

	/**
	 *  人工报告使用 :附件名称
	 */
	private String rptAttachmentName;

	public static PssRptConfEntity toEntity(PssRptConfDto from) {
		if (null == from) {
			return null;
		}
		PssRptConfEntity to = new PssRptConfEntity();
		BeanUtils.copyProperties(from, to);
		to.crteDate = new Date();
		return to;
	}

	public static PssRptConfDto toData(PssRptConfEntity from){
		if(null == from){
			return null;
		}
		PssRptConfDto to = new PssRptConfDto();
		BeanUtils.copyProperties(from, to);
		return to;
	}
}
