package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
public interface WpBaseIndexValService extends IService<WpBaseIndexValEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Map<String, PssCommTotalEntity>> queryList();

    List<Map<String, Object>> queryDetailByCommId(Map<String, Object> condition);

    List<Map<String, Object>> queryIndexTypeByCommId(Integer commId);

    Map<String, Object> analyseType4CommIndexs(Integer commId);

    List<Map<String, Object>> secondPageIndexType(Integer commId);

    List<WpBaseIndexValEntity> getprovinceLastDayMapData(Integer type3CommId,String indexType,String dataStr);

    PageUtils queryPageByDate(Map<String, Object> params);

    Map<String, Object> queryLineChartByCondition(Map<String, Object> params);

    List<PssCommTotalEntity> queryCommListByCommId(Integer commId,String indexType);

    List<WpBaseIndexValEntity> getProvinceMapByCommId(Integer commId,String indexType ,String date);


}

