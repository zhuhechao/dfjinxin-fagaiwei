package io.dfjinxin.common.dto;

import lombok.Data;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/8/26 11:32
 * @Version: 1.0
 */
@Data
public class QueryBaseDto {

    private int pageIndex;//当前页
    private int pageSize;//每页显示条数
    private int start;

    public void startNumber() {
        this.start = (pageIndex - 1) * pageSize;
    }
}
