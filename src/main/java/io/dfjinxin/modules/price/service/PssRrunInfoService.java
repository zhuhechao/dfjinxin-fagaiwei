package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssRrunInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 10:02:41
 */
public interface PssRrunInfoService extends IService<PssRrunInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

