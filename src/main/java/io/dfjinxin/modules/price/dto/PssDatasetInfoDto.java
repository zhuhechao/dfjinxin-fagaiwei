package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@Accessors(chain = true)
public class PssDatasetInfoDto implements Serializable {

    /**
     *
     */
    @TableId
    private String dataSetId;
}
