package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @Author LiangJianCan
 * @Date 2021/4/21 15:13
 * @Description 中国的城市实体类
 */
@Data
public class ChinaAreaInfo implements Serializable {
    @ApiModelProperty(value = "地域id", name = "areaId")
    private Integer areaId;

    @ApiModelProperty(value = "夫id", name = "parentId")
    private Integer parentId;

    @ApiModelProperty(value = "地域名称", name = "areaName")
    private String areaName;

    @ApiModelProperty(value = "子地域", name = "chinaAreaInfos")
    private List<ChinaAreaInfo> chinaAreaInfos;
}
