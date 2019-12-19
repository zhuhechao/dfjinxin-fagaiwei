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

    List<PssPriceEwarnEntity> getDayReportData(Map<String, Object> params);

    List<Map<String, Object>> getDayReportDataForBarImage(Map<String, Object> params);

    //    腾讯-获取配置方案结果分布
    int getProgrammeDistribution();

    Map<String, Object> queryIndexLineData(Integer ewarnTypeId, List<Integer> asList, String startDate, String endDate);

    Map<String, Object> queryIndexAvgByIndexId(Integer indexId, Integer ewarnTypeId);

    Map<String, Object> bg_firstPage_commEwarn();

    Map<String, Object> bg_firstPage_riskInfo();

    List<PssPriceReltEntity> jiaGeYuCe(String foreType, Integer commId);

}

