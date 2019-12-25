package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dfjinxin.common.validator.CustomDoubleSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@Data
@TableName("wp_macro_index_val")
public class WpMcroIndexValEntity implements Serializable {
    private static final long serialVersionUID = 188866008944L;

    private Integer indexId;
    private String indexName;
    private String unit;
    private String indexType;
    private String frequence;
    private String areaName;
    private String sourceName;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double value;
    private String date;

}
