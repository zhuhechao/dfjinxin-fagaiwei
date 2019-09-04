package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 17:32:05
 */
public interface PssIndexReltService extends IService<PssIndexReltEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

