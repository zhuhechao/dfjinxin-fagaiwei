package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;

import java.util.Map;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
public interface PssPriceReltService extends IService<PssPriceReltEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Object> detail(Long id, String dateFrom, String DateTo);
}

