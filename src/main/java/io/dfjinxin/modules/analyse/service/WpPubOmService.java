package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpPubOmEntity;

import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-18 10:56:59
 */
public interface WpPubOmService extends IService<WpPubOmEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Object> getYuQing(Integer commId,String dateFrom ,String dateTo);
}

