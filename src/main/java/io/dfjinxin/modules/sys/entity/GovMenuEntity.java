package io.dfjinxin.modules.sys.entity;


import lombok.Data;

import java.util.List;

@Data
public class GovMenuEntity {

    private String menuId;
    private String menuName;
    private String menuUrl;
    private String parentMenuId;
    private List<GovMenuEntity> children;

}
