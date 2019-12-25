package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssPriceReltDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Data
@TableName("pss_price_relt")
public class PssPriceReltEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;

    private Integer commId;
    private Integer indexId;
    /**
     *
     */
    private String modId;
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

    @TableField(exist = false)
    private String commName;

 /*   public static PssPriceReltEntity toEntity(PssPriceReltDto from) {
        if (null == from) {
            return null;
        }
        PssPriceReltEntity to = new PssPriceReltEntity();
        BeanUtils.copyProperties(from, to);
        return to;
    }

    public static PssPriceReltDto toData(PssPriceReltEntity from) {
        if (null == from) {
            return null;
        }
        PssPriceReltDto to = new PssPriceReltDto();
        BeanUtils.copyProperties(from, to);
        return to;
    }*/
}
