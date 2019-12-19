package io.dfjinxin.modules.price.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/12/18 16:27
 * @Version: 1.0
 */
@Data
public class PriceReltDto {
    private Long id;
    //预测价格
    private BigDecimal forePrice;
    //修正价格
    private BigDecimal reviPrice;
//    实际价格
    private BigDecimal realPrice;

    //修正时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviDate;

    //交易时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dataDate;
}
