package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
public interface PssAnalyInfoService extends IService<PssAnalyInfoEntity> {

    PssAnalyInfoDto saveOrUpdate(PssAnalyInfoDto dto);

    PageUtils queryPage(Map<String, Object> params);
}

