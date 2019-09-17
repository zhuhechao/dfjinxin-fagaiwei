package io.dfjinxin.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/3.
 */
@Data
public class FgDepEntity implements Serializable {
 private static final long serialVersionUID = 1L;


 //所属子级组织机构的外部ID
  private ArrayList<String> childrenOuUuid;


  private String description;


   private ArrayList<String> manager;


    private String organization;


    private String organizationUuid;


    private String parentUuid;


    private String regionId;


    private boolean rootNode;

    private  String type;


    private String requestType;


    private String operateObject;

}
