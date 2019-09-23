package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssAnalyInfoDao;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("pssAnalyInfoService")
public class PssAnalyInfoServiceImpl extends ServiceImpl<PssAnalyInfoDao, PssAnalyInfoEntity> implements PssAnalyInfoService {

    @Autowired
    private PssDatasetInfoDao pssDatasetInfoDao;

    @Override
    public PssAnalyInfoDto saveOrUpdate(PssAnalyInfoDto dto) {
        PssAnalyInfoEntity entity = PssAnalyInfoEntity.toEntity(dto);

        super.saveOrUpdate(entity);
        return PssAnalyInfoEntity.toData(entity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssAnalyInfoEntity> page = this.page(
                new Query<PssAnalyInfoEntity>().getPage(params),
                new QueryWrapper<PssAnalyInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PssAnalyReltEntity> getAnalyWayByBussType(Integer bussType) {
        if (bussType == null) {
            return null;
        }
        QueryWrapper where = new QueryWrapper();
        where.eq("buss_type", bussType);
        return baseMapper.selectList(where);
    }

    @Override
    public List<PssDatasetInfoEntity> getDataSetByAnalyWay(String analyWay) {
        if(StringUtils.isBlank(analyWay)){
            return null;
        }
        return pssDatasetInfoDao.getDataSetByAnalyWay(analyWay);
    }

}
