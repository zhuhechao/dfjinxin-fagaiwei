package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 14:00:37
 */
public interface WpAsciiInfoService extends IService<WpAsciiInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WpAsciiInfoEntity> getInfoByCodeId(String codeSimple);

    List<WpAsciiInfoEntity> getInfoAll();
}

