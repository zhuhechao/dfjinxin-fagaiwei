package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Data
@TableName("pss_user_dep")
public class SysDepEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String depId;

    private String superDepId;

    private String depName;

    private  int depState;

    private Timestamp creDate;

    private Timestamp updDate;

    @TableField(exist = false)
    private boolean status;

    @TableField(exist = false)
    private String superDepName;

}
