package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-22 16:56:09
 */
@Data
@TableName("pss_ewarn_conf")
public class PssEwarnConfEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer ewarnId;

    private Integer rschId;
    /**
     *
     */
    private String ewarnName;
    /**
     *
     */
    private String ewarnTypeId;
    /**
     *
     */
    private BigDecimal ewarnUlmtRed;
    /**
     *
     */
    private BigDecimal ewarnLlmtRed;
    /**
     *
     */
    private BigDecimal ewarnUlmtOrange;
    /**
     *
     */
    private BigDecimal ewarnLlmtOrange;
    /**
     *
     */
    private BigDecimal ewarnUlmtYellow;
    /**
     *
     */
    private BigDecimal ewarnLlmtYellow;
    /**
     *
     */
    private BigDecimal ewarnUlmtGreen;
    /**
     *
     */
    private BigDecimal ewarnLlmtGreen;
    /**
     *
     */
    private String ewarnTerm;
    /**
     *
     */
    private Integer ewarnDays;
    /**
     *
     */
    private Date beginDate;
    /**
     *
     */
    private Date endDate;
    /**
     *
     */
    private Date crteDate;
    /**
     *
     */
    private String delFlag;
    /**
     *
     */
    private String crteName;
    /**
     *
     */
    private String remarks;

    private String colorValRed;

    private String colorValOrange;

    private String colorValYellow;

    private String colorValGreen;

}
