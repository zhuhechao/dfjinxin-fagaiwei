package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-23 15:38:58
 */
public interface PssCommTotalService extends IService<PssCommTotalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,List<PssCommTotalEntity>> queryCommType();
}

