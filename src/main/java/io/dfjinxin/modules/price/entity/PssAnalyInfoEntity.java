package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@Data
@TableName("pss_analy_info")
@ApiModel(value = "分析信息对象")
public class PssAnalyInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "分析id", name = "analyId", required = true)
    private Integer analyId;
    /**
     *
     */
    @ApiModelProperty(value = "分析名称", name = "analyName", required = true)
    private String analyName;
    /**
     *
     */
    @ApiModelProperty(value = "分析类型", name = "analyWay", required = true)
    private String analyWay;
    /**
     *
     */
    @ApiModelProperty(value = "数据集id", name = "dataSetId", required = true)
    private Integer dataSetId;
    /**
     *
     */
    @ApiModelProperty(value = "自变量", name = "indeVar", required = true)
    private String indeVar;
    /**
     *
     */
    @ApiModelProperty(value = "因变量", name = "depeVar", required = true)
    private String depeVar;
    /**
     *
     */
    @ApiModelProperty(value = "分析描述", name = "remarks", required = true)
    private String remarks;
    /**
     *
     */
    @ApiModelProperty(value = "创建时间", name = "crteTime", required = true)
    private Date crteTime;

    // 1:相关性，2：因果
    @ApiModelProperty(value = "业务类型", name = "bussType", required = true)
    private Integer bussType;

    @TableField(exist = false)
    private String dataSetName;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runTime;


    public static PssAnalyInfoEntity toEntity(PssAnalyInfoDto from) {
        if (null == from) {
            return null;
        }
        PssAnalyInfoEntity to = new PssAnalyInfoEntity();
        BeanUtils.copyProperties(from, to);
        to.indeVar = StringUtils.join(from.getIndeVar(), ",");
//        if ("一般相关性分析".equals(from.getAnalyWay())) {
//            to.depeVar = to.indeVar;
//        }
        if(to.getBussType()==null)
            to.setBussType(1);//默认一般相关性分析
        return to;
    }

    public static PssAnalyInfoDto toData(PssAnalyInfoEntity from){
        if(null == from){
            return null;
        }
        PssAnalyInfoDto to = new PssAnalyInfoDto();
        BeanUtils.copyProperties(from, to);
        to.setIndeVar(from.indeVar.split(","));
        return to;
    }
}
