package io.dfjinxin.modules.sys.entity;

import lombok.Data;

import java.util.List;

@Data
public class GovRootMenuEntity {

    private String rootMenuId;
    private String rootMenuName;
    private String outLink;
    private String menuUrl;
    private List<SysMenuEntity> children;

}
