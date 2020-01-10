package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.WpUpdateInfoEntity;

import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2020-01-10 15:52:24
 */
public interface WpUpdateInfoService extends IService<WpUpdateInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Long getEverydayInfoTotal();
}

