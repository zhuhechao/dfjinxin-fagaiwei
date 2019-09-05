package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class PssIndexReltDto implements Serializable {

    @ApiModelProperty(value = "指标名称", name = "indexName", required = true)
    private String indexName;

    @ApiModelProperty(value = "日期", name = "dataDate", required = true)
    private Date dataDate;

    @ApiModelProperty(value = "数据来源(e.g:中经网)", name = "dataSource", required = true)
    private String dataSource;

    @ApiModelProperty(value = "预测指数", name = "foreIndex", required = true)
    private BigDecimal foreIndex;

    @ApiModelProperty(value = "指标值", name = "indexVal", required = true)
    private BigDecimal indexVal;
}
