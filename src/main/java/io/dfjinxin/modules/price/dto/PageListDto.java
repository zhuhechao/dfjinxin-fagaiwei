package io.dfjinxin.modules.price.dto;

import lombok.Data;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/8/22 16:36
 * @Version: 1.0
 */

@Data
public class PageListDto {

    private String ewarnTypeId;// 预警类型id
    private String ewarnName;// 预警名称

    private int pageIndex;//当前页
    private int pageSize;//每页显示条数
    private int start;

    public void startNumber() {
        this.start = (pageIndex - 1) * pageSize;
    }
}
