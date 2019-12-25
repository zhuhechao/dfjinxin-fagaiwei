package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dfjinxin.common.validator.CustomDoubleSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@Data
@TableName("wp_base_index_val")
public class WpBaseIndexValEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer indexId;
    /**
     *
     */
    private Integer commId;
    /**
     *
     */
    private String indexName;
    /**
     *
     */
    private String indexType;
    /**
     *
     */
    private String areaName;
    /**
     *
     */
    private String frequence;
    /**
     *
     */
    private String unit;
    /**
     *
     */
    private String sourceName;
    /**
     *
     */
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double value;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;


    @TableField(exist = false)
    private String commName;
    @TableField(exist = false)
    private String tongBi;

    @TableField(exist = false)
    private String huanBi;
}
