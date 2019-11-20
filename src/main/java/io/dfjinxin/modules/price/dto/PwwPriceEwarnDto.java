package io.dfjinxin.modules.price.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Desc: 首页-商品预警趋势 dto
 * @Author: z.h.c
 * @Date: 2019/11/20 14:03
 * @Version: 1.0
 */
@Data
public class PwwPriceEwarnDto {

    private String ewarnDate;
    private int ewarnCount;

}
