package io.dfjinxin.modules.price.dao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/11/14 17:03
 * @Version: 1.0
 */

@Data
@Accessors(chain = true)
public class WpCommPriDto implements Serializable {

    private static final long serialVersionUID = 3434343333331L;

    private String indexName;

    private Integer indexId;

    private BigDecimal value;

    private String unit;

    private Date dataTime;
}
