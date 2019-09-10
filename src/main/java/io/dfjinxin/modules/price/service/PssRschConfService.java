package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssRschConfDto;
import io.dfjinxin.modules.price.entity.PssRschConfEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-10 09:22:42
 */
public interface PssRschConfService extends IService<PssRschConfEntity> {

    PssRschConfDto saveOrUpdate(PssRschConfDto dto);

    PageUtils queryPage(Map<String, Object> params);
}

