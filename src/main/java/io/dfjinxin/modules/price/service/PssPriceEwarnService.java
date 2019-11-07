package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 17:21:32
 */
public interface PssPriceEwarnService extends IService<PssPriceEwarnEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,Object> queryList();

    List<Object> queryDetail(Integer commId,Integer ewarnTypeId);

//    Map<String,Object> queryType3Warn();

    Map<String, Object> converZF(Integer commId);

    Map<String, Object> firstPageView();

    Map<String, Object> firstPageView2();

    List<PssPriceEwarnEntity> getDayReportData(Map<String,Object> params);
    List<Map<String,Object>> getDayReportDataForBarImage(Map<String,Object> params);

//    腾讯-获取配置方案结果分布
    int getProgrammeDistribution();
}

