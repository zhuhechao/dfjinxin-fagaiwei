package io.dfjinxin.modules.price.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:22:40
 */
@Data
@Accessors(chain = true)
public class PssRptInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rptId;
    /**
     *
     */
    private String rptName;
    /**
     *
     */
    private Integer commId;
    /**
     *
     */
    private Integer rptFreq;
    /**
     *
     */
    private Integer rptType;
    /**
     *
     */
    private Integer statCode;
    /**
     *
     */
    private Date rptDate;
    /**
     *
     */
    private Date crteTime;
    /**
     *
     */
    private String rptPath;
    /**
     *
     */
    private String rptStatus;
    /**
     *
     */
    private byte[] rptFile;
    /**
     *
     */
    private Date delTime;
}
