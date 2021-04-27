package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiangJianCan
 * @Date 2021/4/26 18:57
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommMessage implements Serializable {

    @ApiModelProperty(value = "商品名称", name = "commName")
    private String commName;

    @ApiModelProperty(value = "省份", name = "commArea")
    private String commArea;

    @ApiModelProperty(value = "价格", name = "commPrice")
    private BigDecimal commPrice;

    @ApiModelProperty(value = "单位", name = "commUnit")
    private String commUnit;

    @ApiModelProperty(value = "涨跌幅", name = "commRange")
    private String commRange;
}
