package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PageListDto;
import io.dfjinxin.modules.price.entiry.PssEwarnConfEntity;

import java.util.Map;

/**
 * 价格预警配置表
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-22 14:55:38
 */
public interface PssEwarnConfService extends IService<PssEwarnConfEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageList(PageListDto pageListDto);

    PssEwarnConfEntity queryEntityByEwarnId(String ewarnId);
}
