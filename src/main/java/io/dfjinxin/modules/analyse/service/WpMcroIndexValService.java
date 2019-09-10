package io.dfjinxin.modules.analyse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;

import java.util.Map;

/**
 * 
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
public interface WpMcroIndexValService extends IService<WpMcroIndexValEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

