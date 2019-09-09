package io.dfjinxin.modules.price.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
    private String rptId;

    @ApiModelProperty(value = "报告名称", name = "rptName", required = true)
    private String rptName;

    @ApiModelProperty(value = "报告频度", name = "rpt_freq", required = true)
    private Integer rptFreq;

    @ApiModelProperty(value = "商品id", name = "commId", required = true)
    private Integer commId;

    @ApiModelProperty(value = "0 自动 1 手工", name = "rptType", required = true)
    private Integer rptType;

    @ApiModelProperty(value = "创建者", name = "crteName", required = true)
    private String crteName;

    @ApiModelProperty(value = "备注", name = "rptRemarks", required = true)
    private String rptRemarks;

    @ApiModelProperty(value = "模板路径", name = "rptPath", required = true)
    private String rptPath;

    @ApiModelProperty(value = "1未审核 2审核通过 3审核未通过 4无需审核", name = "rptStatus", required = true)
    private Integer rptStatus;
}
