package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PssDatasetInfoDto implements Serializable {

    /**
     *
     */
    @TableId
    private String dataSetId;
}
