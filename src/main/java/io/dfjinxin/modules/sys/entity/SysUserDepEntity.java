package io.dfjinxin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Data
@TableName("pss_user_dep_rela")
public class SysUserDepEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private int relaId;

    private String userId;

    private String depId;
}
