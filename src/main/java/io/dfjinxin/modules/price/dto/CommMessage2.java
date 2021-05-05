package io.dfjinxin.modules.price.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiangJianCan
 * @Date 2021/4/30 17:02
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommMessage2 implements Serializable {

    @ApiModelProperty(value = "商品名称", name = "commName")
    private String commName;

    @ApiModelProperty(value = "商品地区", name = "commArea")
    private String commArea;

    @ApiModelProperty(value = "价格", name = "commPrice")
    private BigDecimal commPrice;

    @ApiModelProperty(value = "单位", name = "commUnit")
    private String commUnit;

    @ApiModelProperty(value = "涨跌幅", name = "commRange")
    private String commRange;

    @ApiModelProperty(value = "省名称", name = "provinceName")
    private String provinceName;

    @ApiModelProperty(value = "地区Id", name = "areaId")
    private String areaId;

    @ApiModelProperty(value = "父级地区Id", name = "parentId")
    private String parentAreaId;


}
