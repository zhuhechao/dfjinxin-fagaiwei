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

    List<PssPriceEwarnEntity> queryList();

    List<PssPriceEwarnEntity> queryDetail(Integer commId);
}

