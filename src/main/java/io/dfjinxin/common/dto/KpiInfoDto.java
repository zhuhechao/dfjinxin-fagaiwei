package io.dfjinxin.common.dto;

import lombok.Data;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/9/10 17:48
 * @Version: 1.0
 */
@Data
public class KpiInfoDto {
    private String indexName;
    private String indexVal;
    private String indexUnit;
    private String dataTime;
    private Integer commId;
    private Integer indexId;
}
