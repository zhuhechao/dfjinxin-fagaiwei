package io.dfjinxin.modules.price.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dfjinxin.common.validator.CustomDoubleSerializer;
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

    /**
     * 商品名称
     */
    private String commName;
    /**
     *
     */
    private String modId;

    /**
     * 模型名称
     */
    private String modName;

    /**
     * 平均绝对百分比误差
     */
    private String mape;

    /**
     * 算法名称
     */
    private String algoName;

    /**
     * 价格单位
     */
    private String priUnit;
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
    private String foreType;
    /**
     *
     */
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private BigDecimal forePrice;
    /**
     *
     */
    private Date foreTime;
    /**
     *
     */

    @JsonSerialize(using = CustomDoubleSerializer.class)
    private BigDecimal reviPrice;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    private BigDecimal realPrice;
    /**
     *
     */
    private Date reviTime;

}
