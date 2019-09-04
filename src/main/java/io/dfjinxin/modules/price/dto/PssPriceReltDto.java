package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@Accessors(chain = true)
public class PssPriceReltDto implements Serializable {

    /**
     *
     */
    private Integer commId;

    private String commName;
    /**
     *
     */
    private Integer modId;
    private String modName;

    private String mape;
    /**
     *
     */
    private Integer dataSetId;
    /**
     *
     */
    private Date dataDate;
    /**
     *
     */
    private String modType;
    /**
     *
     */
    private BigDecimal forePrice;
    /**
     *
     */
    private Date foreTime;
    /**
     *
     */
    private BigDecimal reviPrice;
    /**
     *
     */
    private Date reviTime;

}
