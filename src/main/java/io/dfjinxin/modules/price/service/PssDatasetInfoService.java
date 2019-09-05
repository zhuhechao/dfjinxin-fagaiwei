package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
public interface PssDatasetInfoService extends IService<PssDatasetInfoEntity> {

    PssDatasetInfoDto saveOrUpdate(PssDatasetInfoDto dto);

    List<PssDatasetInfoDto> listAll();
}

