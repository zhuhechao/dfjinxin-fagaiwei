package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssRptInfoDto;
import io.dfjinxin.modules.price.entity.PssRptInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:22:40
 */
public interface PssRptInfoService extends IService<PssRptInfoEntity> {

    PssRptInfoDto saveOrUpdate(PssRptInfoDto dto);

    PageUtils queryPage(Map<String, Object> params);
}

