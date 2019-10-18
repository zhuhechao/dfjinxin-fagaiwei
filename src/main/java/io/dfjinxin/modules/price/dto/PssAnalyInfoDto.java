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
public class PssAnalyInfoDto implements Serializable {

    @ApiModelProperty(value = "分析ID", name = "analyId", required = true)
    private Integer analyId;

    @ApiModelProperty(value = "数据集id", name = "dataSetId", required = true)
    private Integer dataSetId;

    @ApiModelProperty(value = "分析名称", name = "analyName", required = true)
    private String analyName;

    @ApiModelProperty(value = "分析描述", name = "remarks", required = true)
    private String remarks;

    @ApiModelProperty(value = "分析类型", name = "analyWay", required = true, example = "一般相关性分析")
    private String analyWay;

    @ApiModelProperty(value = "变量", name = "vars", required = true)
    private String[] indeVar;

    @ApiModelProperty(value="他变量" ,name="depeVar",required = false)
    private String depeVar;

    @ApiModelProperty(value="业务类型" ,name="bussType",required = true)
    private Integer bussType;

}
