package io.dfjinxin.modules.analyse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/12/19 15:27
 * @Version: 1.0
 */
@Data
public class PriceReltLineChartsDto implements Serializable {

    private static final long serialVersionUID = 90986609000331L;

    private Integer commId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    private BigDecimal val;


}
