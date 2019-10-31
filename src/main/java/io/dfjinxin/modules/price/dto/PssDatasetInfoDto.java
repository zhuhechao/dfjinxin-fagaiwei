package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@Accessors(chain = true)
public class PssDatasetInfoDto implements Serializable {

    @ApiModelProperty(value = "数据集id", name = "dataSetId", required = true)
    private Integer dataSetId;

    @ApiModelProperty(value = "数据集类型", name = "dataSetType", required = true)
    private Integer dataSetType;

    @ApiModelProperty(value = "数据集名称", name = "dataSetName", required = true)
    private String dataSetName;

//    @ApiModelProperty(value = "关联sql", name = "tabName", required = true)
//    private String tabName;

    @ApiModelProperty(value = "字段取值", name = "indeVar", required = true)
    private Map<String, Object> indeVar;

    @ApiModelProperty(value="字段名称",name="indeName",required = false)
    private Map<String,Object> indeName;
}
