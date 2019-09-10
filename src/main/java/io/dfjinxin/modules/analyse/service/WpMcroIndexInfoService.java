package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
public interface WpMcroIndexInfoService extends IService<WpMcroIndexInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WpMcroIndexInfoEntity getType();

    List<WpMcroIndexInfoEntity> getName();
}

