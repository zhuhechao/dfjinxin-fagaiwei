package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class PssAnalyInfoDto implements Serializable {

    @ApiModelProperty(value = "分析ID", name = "analyId", required = true)
    private Integer analyId;

    @ApiModelProperty(value = "数据集id", name = "dataSetId", required = true)
    private Integer dataSetId;

    @ApiModelProperty(value = "分析名称", name = "analyName", required = true)
    private String analyName;

    @ApiModelProperty(value = "分析描述", name = "remarks", required = true)
    private String remarks;

    @ApiModelProperty(value = "变量", name = "vars", required = true)
    private List<String> vars;
}
