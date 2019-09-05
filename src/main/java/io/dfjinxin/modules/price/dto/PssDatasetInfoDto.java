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
public class PssDatasetInfoDto implements Serializable {

    @ApiModelProperty(value = "数据集id", name = "dataSetId", required = true)
    private Integer dataSetId;

    @ApiModelProperty(value = "数据集名称", name = "dataSetName", required = true)
    private String dataSetName;

    @ApiModelProperty(value = "关联sql", name = "realSql", required = true)
    private String realSql;

    @ApiModelProperty(value = "自变量", name = "indeVar", required = true)
    private String[] indeVar;
}
