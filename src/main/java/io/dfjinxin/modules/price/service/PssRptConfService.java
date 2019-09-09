package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssRptConfDto;
import io.dfjinxin.modules.price.entity.PssRptConfEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:13:08
 */
public interface PssRptConfService extends IService<PssRptConfEntity> {

    PssRptConfDto saveOrUpdate(PssRptConfDto dto);

    PageUtils queryPage(Map<String, Object> params);
}

