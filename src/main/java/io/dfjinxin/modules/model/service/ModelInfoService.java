package io.dfjinxin.modules.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.model.entity.ModelInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 模型基本信息
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-25 13:48:01
 */
public interface ModelInfoService extends IService<ModelInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryByPage(Map<String, Object> params);

    List<String> getAlgorithm();
}

