package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
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
public interface WpCommIndexValService extends IService<WpCommIndexValEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Map<String, PssCommTotalEntity>> queryList();

    List<Map<String, Object>> queryDetailByCommId(Map<String, Object> condition);

    List<Map<String, Object>> queryIndexTypeByCommId(Integer commId);

    List<Map<String, Object>> queryLevel4CommInfo(Integer commId);

    Map<String, Object> analyseType4CommIndexs(Integer commId);
}

