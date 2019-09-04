package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssForeModResultEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 09:33:51
 */
public interface PssForeModResultService extends IService<PssForeModResultEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

