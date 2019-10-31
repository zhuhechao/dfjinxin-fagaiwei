package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("pssDatasetInfoService")
public class PssDatasetInfoServiceImpl extends ServiceImpl<PssDatasetInfoDao, PssDatasetInfoEntity> implements PssDatasetInfoService {

    @Override
    public List<PssDatasetInfoEntity> listAll() {
        return baseMapper.selectList(new QueryWrapper());
    }
}
