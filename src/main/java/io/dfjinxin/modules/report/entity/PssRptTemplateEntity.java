package io.dfjinxin.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PssRptTemplateEntity
 * @Author：lym 863968235@qq.com
 * @Date： 2019/12/27 17:04
 * 修改备注：
 */
@Data
@TableName("pss_rpt_template")
public class PssRptTemplateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //模板id
    @TableId
    private Integer tempId;

    //模板名称
    private String rptName;

    //模板路径
    private String rptPath;



}
