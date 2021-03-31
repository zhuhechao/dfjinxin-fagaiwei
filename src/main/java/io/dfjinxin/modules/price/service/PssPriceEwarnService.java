package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;

import java.util.List;
import java.util.Map;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 17:21:32
 */
public interface PssPriceEwarnService extends IService<PssPriceEwarnEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Object> queryList();

    Map<String, Object> queryconfByewarnTypeId(Integer commId, Integer ewarnTypeId);

    Map<String, Object> secondPageDetail(Integer commId);

    Map<String, Object> firstPageView(boolean queryHive);

    Map<String, Object> indexPageViewLeft(Map<String, Object> params,boolean queryHive);

    Map<String, Object> indexPageViewCenter();

    List<PssPriceEwarnEntity> getDayReportData(Map<String, Object> params);

    List<Map<String, Object>> getDayReportDataForBarImage(Map<String, Object> params);

    //    腾讯-获取配置方案结果分布
    int getProgrammeDistribution();

    Map<String, Object> queryIndexLineData(Integer ewarnTypeId, List<Integer> asList, String startDate, String endDate);

    Map<String, Object> queryIndexAvgByIndexId(Integer indexId, Integer ewarnTypeId);

    Map<String, Object> ewarmInfo(Integer commId);

    Map<String, Object> bg_firstPage_commEwarn();

    Map<String, Object> bg_firstPage_riskInfo();

    List<PssPriceReltEntity> jiaGeYuCe(String foreType, Integer commId);

    Map<String, Object> queryIndexLineData2(Integer ewarnTypeId, List<Integer> asList, String startDate, String endDate);

    List<Map<String, Object>> warningDistribution(Map<String, Object> params);

    List<Map<String, Object>> warningIndexDate(Map<String, Object> params);

    Map<String, Object> getEwarValue(Map<String, Object> params);

    List<Map<String, Object>> viewBy(Map<String, Object> params);

    Map<String, Object> fore(Map<String, Object> params);

    List<Map<String, Object>> foreData(Map<String, Object> params);

    Map<String, Object> ewarmMap(Map<String, Object> params);

//    Map<String, Object> viewMap(Map<String, Object> params);
}

