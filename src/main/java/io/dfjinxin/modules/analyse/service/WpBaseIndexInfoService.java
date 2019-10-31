package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:20
 */
public interface WpBaseIndexInfoService extends IService<WpBaseIndexInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WpBaseIndexInfoEntity> getIndexNameByType(Integer commId);

    List<WpBaseIndexInfoEntity> getIndexTreeByCommId(Integer commId);

    List<WpBaseIndexInfoEntity> getIndexTreeByIds(String []ids);
}

