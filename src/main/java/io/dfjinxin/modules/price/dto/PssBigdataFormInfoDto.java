package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PssBigdataFormInfoDto implements Serializable {

    /**
     *
     */
    @TableId
    private Integer formId;

    @ApiModelProperty(value = "表名", name = "formName", required = true)
    private String formName;

    @ApiModelProperty(value = "描述", name = "formNameC", required = true)
    private String formNameC;

    @ApiModelProperty(value = "字段", name = "fields", required = true)
    private List<Map<String, String>> fields;
}
