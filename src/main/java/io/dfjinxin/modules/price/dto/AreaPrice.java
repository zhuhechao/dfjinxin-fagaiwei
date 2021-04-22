package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiangJianCan
 * @Date 2021/4/21 15:48
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaPrice implements Serializable {

    @ApiModelProperty(value = "商品id", name = "commId")
    private String commId;

    @ApiModelProperty(value = "地域名称", name = "areaName")
    private String areaName;

    @ApiModelProperty(value = "金额", name = "price")
    private BigDecimal price;
}
