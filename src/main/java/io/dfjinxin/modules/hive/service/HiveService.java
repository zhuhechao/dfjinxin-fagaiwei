package io.dfjinxin.modules.hive.service;

import java.util.List;
import java.util.Map;

public interface HiveService {

    List<Map<String, Object>> selectData(String sql);

    List<Map<String, Object>> pageData(String sql, String sortStr, Integer page, Integer limit);

    Object checkFunction(String funcName, String param, String type);
}
