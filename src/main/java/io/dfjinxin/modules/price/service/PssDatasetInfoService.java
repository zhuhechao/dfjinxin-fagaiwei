package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.DataSetIndexInfoDto;
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

    List<PssDatasetInfoEntity> listAll();
    PssDatasetInfoEntity getPssDatasetInfoById(int id);
    void setPssDatasetInfoIndeName(PssDatasetInfoEntity pssDatasetInfoEntity);

    List<DataSetIndexInfoDto> getIndexInfoByDataSetIndeVal(String indeVal);

    PageUtils queryByPage(Map<String, Object> params);

    List<PssDatasetInfoEntity> getDataSetList();

    void converValAndName(PssDatasetInfoEntity pssDatasetInfoEntity);
}

