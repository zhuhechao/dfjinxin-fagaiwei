package io.dfjinxin.modules.price.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Desc: 数据集-indeval中指标id对应指标信息dto
 * @Author: z.h.c
 * @Date: 2019/11/28 18:14
 * @Version: 1.0
 */

@Data
public class DataSetIndexInfoDto implements Serializable {

    private static final long serialVersionUID = 8899900000331L;

    private Integer indexId;

    private Integer commId;

    private String areaName;

    private String indexName;

    private String commName;

    private String frequence;
}
