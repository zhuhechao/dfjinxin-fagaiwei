package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName DataSourcesEntity
 * @Author：lym 863968235@qq.com
 * @Date： 2019/9/19 16:32
 * 修改备注：
 */
@Data
@TableName("pss_data_sources_access")
public class PssDataSourcesEntity {

    //数据源id
    @TableId
    private Integer dataId;

    //数据源名称
    private String dataName;

    //数据源类型
    private Integer dataType;

    //实例名
    private String exampleName;

    //用户名
    private String userName;

    //密码
    private String password;

    //数据源地址
    private String dataAddress;

    //数据源端口
    private String dataPort;

    //数据源描述
    private String dataRemark;

    //数据源接入状态
    private Integer accessState;








}
