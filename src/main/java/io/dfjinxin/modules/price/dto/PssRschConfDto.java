package io.dfjinxin.modules.price.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class PssRschConfDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", name = "rschId", required = true)
    private Integer rschId;

    @ApiModelProperty(value = "调度名称", name = "rschName", required = true)
    private String rschName;

    @ApiModelProperty(value = "调度类型 1 模型调度 2 报告调度 3 预警调度", name = "rschType", required = true)
    private Integer rschType;

    @ApiModelProperty(value = "调度频度", name = "rschFreq", required = true)
    private Integer rschFreq;

    @ApiModelProperty(value = "执行方式", name = "execType", required = true)
    private char execType;

    @ApiModelProperty(value = "执行时间", name = "execTime", required = true, example = "2019-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date execTime;

    @ApiModelProperty(value = "开始时间", name = "startTime", required = true, example = "2019-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", name = "endTime", required = true, example = "2019-01-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "执行条件", name = "execCdt", required = true)
    private String execCdt;

    @ApiModelProperty(value = "调度说明", name = "rschRemarks", required = true)
    private String rschRemarks;
}
