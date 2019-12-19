package io.dfjinxin.modules.analyse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/12/18 13:27
 * @Version: 1.0
 */
@Data
public class CommYuQingDto implements Serializable {

    private static final long serialVersionUID = 6609000331L;

    private Integer commId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    private Long heatTrend;
    private Long val;


}
