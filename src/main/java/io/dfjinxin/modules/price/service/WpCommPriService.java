package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.WpCommPriEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 17:23:11
 */
public interface WpCommPriService extends IService<WpCommPriEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<WpCommPriEntity> getData (Map<String, Object> params) ;
}

