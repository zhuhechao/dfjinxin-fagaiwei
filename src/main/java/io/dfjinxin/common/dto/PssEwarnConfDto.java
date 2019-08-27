package io.dfjinxin.common.dto;

import lombok.Data;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/8/22 16:36
 * @Version: 1.0
 */

@Data
public class PssEwarnConfDto extends QueryBaseDto {

    private String ewarnTypeId;// 预警类型id
    private String ewarnName;// 预警名称

}
