package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
public interface WpMcroIndexInfoService extends IService<WpMcroIndexInfoEntity> {

//    List<Map<String, Object>> getAreaName();

//    List<WpMcroIndexValEntity> queryIndexVals(String areaName,String indexId, String dateFrom, String dateTo);

    List<WpMcroIndexInfoEntity> getIndexTreeByType();

    List<WpMcroIndexInfoEntity> getIndexTreeByIds(String [] ids);

    Map<String, Object> queryByIndexType(String indexType);

    List<Map<String, Object>> queryIndexType();
}

