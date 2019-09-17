package io.dfjinxin.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/3.
 */
@Data
public class FgRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    //组名称
    private String displayName;

   //所属父级组织机构的uuid或外部ID
    private String ouUuid;

    //组对应的组织单位
    private ArrayList<Map<String,Object>> belongs;

    //组成员ID
    private ArrayList<Map<String,Object>> members;
}
