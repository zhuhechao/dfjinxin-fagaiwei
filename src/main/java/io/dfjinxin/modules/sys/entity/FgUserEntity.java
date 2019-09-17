package io.dfjinxin.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * 发改认证后跳转到本系统所带参数
 */
@Data
public class FgUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    private ArrayList<Map<String,Object>> belongs;

    private String displayName;

    private ArrayList<Map<String,Object>> emails;

    private String extendField;

    private String externalld;

    private String id;

    private String password;

    private ArrayList<Map<String,Object>> phoneNumbers;

    private String userName;

    private String requestType;

    private String operateObject;

}
