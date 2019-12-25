package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dfjinxin.common.validator.CustomDoubleSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * modify by zhc 11.14
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 17:23:11
 */
@Data
@TableName("wp_comm_pri_org")
public class WpCommPriOrgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String indexName;

    private Integer indexId;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    private BigDecimal value;

    private String unit;

    private Date dataTime;

    private String areaName;

}
