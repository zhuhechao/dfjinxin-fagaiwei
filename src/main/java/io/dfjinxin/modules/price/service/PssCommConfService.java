package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssCommConfEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 19:33:54
 */
public interface PssCommConfService extends IService<PssCommConfEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveCommConf(Integer commId,List<Integer> ewarnIds,List<Integer> indexIds);

    List<PssCommConfEntity> getCommConfByParms(Integer commId, List<Integer> ewarnIds, List<Integer> indexIds);

    void deleteCommConf(Integer confId);

    void saveCommomJob(Integer commId, List<Integer> ewarnIds, List<Integer> indexIds);

    List<PssCommConfEntity> queryByewarnTypeId(Integer commId, Integer ewarnTypeId);
}

