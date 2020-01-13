package io.dfjinxin.modules.report.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import io.dfjinxin.modules.report.entity.PssRptTemplateEntity;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@Accessors(chain = true)
public class PssRptConfDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "报告id", name = "rptId", required = true)
    private Long rptId;

    @ApiModelProperty(value = "商品ID", name = "commId", required = true)
    private Integer commId;

    @ApiModelProperty(value = "商品名称", name = "commName", required = true)
    private String commName;

    /**
     * 报告类型 0 自动 1 手工
     */
    @ApiModelProperty(value = "0 自动 1 手工", name = "rptType", required = true)
    private String rptType;
    /**
     * 报告频度 D:日  W:周  TD:旬  M:月  Q:季  Y:年
     */
    @ApiModelProperty(value = "报告频度D:日  W:周  TD:旬  M:月  Q:季  Y:年", name = "rpt_freq", required = true)
    private String rptFreq;
    /**
     * 调度配置id 类型为自动  时有值 手动时为空
     */
    @ApiModelProperty(value = "调度配置id 类型为自动  时有值 手动时为空", name = "rschId", required = true)
    private Integer rschId;


    /**
     * 报告日期
     */
    @ApiModelProperty(value = "报告日期", name = "rptDate", required = true)

    private Date rptDate;
    /**
     * 报告名称
     */
    @ApiModelProperty(value = "报告名称", name = "rptName", required = true)
    private String rptName;
    /**
     * 统计区域代码
     */
    private String statCode;
    /**
     * 模板路径
     */
    @ApiModelProperty(value = "模板路径", name = "rptPath", required = false)
    private String rptPath;
    /**
     * 生成时间
     */
    private Date crteDate;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", name = "crteName", required = true)
    private String crteName;
    /**
     * 报告状态 0 正常 1 删除
     */
    @ApiModelProperty(value = "报告状态 0 正常 1 删除", name = "rptStatus", required = true)
    private String rptStatus;
    /**
     * 报告备注
     */
    @ApiModelProperty(value = "备注", name = "rvewRemarks", required = true)
    private String rvewRemarks;
    /**
     * 附件路径  人工报告使用
     */
    @ApiModelProperty(value = "附件路径", name = "rptAttachmentPath", required = false)

    private String rptAttachmentPath;
    /**
     * 人工报告 0不审核，1提交审核
     */
    @ApiModelProperty(value = "人工报告 0不审核，1提交审核", name = "revStatus", required = true)
    private String revStatus;

    /**
     * 调度模板名称 :自动报告使用
     */
    @ApiModelProperty(value = "调度模板名称 :自动报告使用 文件上传后返回", name = "rptTemplateName", required = false)

    private String rptTemplateName;

    /**
     *  人工报告使用 :附件名称
     */
    @ApiModelProperty(value = "人工报告使用 :附件名称 文件上传后返回", name = "rptAttachmentName", required = false)

    private String rptAttachmentName;

    @ApiModelProperty(value = "模板id", name = "tempId", required = false)
    private Integer tempId;
}
