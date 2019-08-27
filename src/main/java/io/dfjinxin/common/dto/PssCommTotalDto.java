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

    private String commId1;// 商品类型
    private String commId2;// 商品大类
    private String commId3;// 商品名称

}
