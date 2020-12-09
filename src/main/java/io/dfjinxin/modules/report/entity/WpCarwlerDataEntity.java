package io.dfjinxin.modules.report.entity;

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
@TableName("wp_crawler_data")
public class WpCarwlerDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    //模板名称
    private String data_dt;

    //模板路径
    private String title;

    //模板名称
    private String link;

    //模板名称
    private String web;



}
