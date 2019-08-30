package io.dfjinxin.common.dto;

import lombok.Data;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/8/26 11:33
 * @Version: 1.0
 */
@Data
public class PssCommTotalDto extends QueryBaseDto {

    private String commLevelCode_0;// 商品类型
    private String commLevelCode_1;// 商品大类
    private String commLevelCode_2;// 商品名称

}
