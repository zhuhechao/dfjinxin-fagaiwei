package io.dfjinxin.modules.hive.service;

import java.util.List;
import java.util.Map;

public interface HiveService {

    List<Map<String, Object>> selectData(String sql);

    List<Map<String, Object>> pageData(String sql, String sortStr, Integer page, Integer limit);

    Object checkFunction(String funcName, String param, String type);

    /**
    * @Desc:  删除hive表
    * @Param: [tableName]
    * @Return: void
    * @Author: z.h.c
    * @Date: 2019/11/22 17:02
    */
    void dropTable(String tableName);
}
