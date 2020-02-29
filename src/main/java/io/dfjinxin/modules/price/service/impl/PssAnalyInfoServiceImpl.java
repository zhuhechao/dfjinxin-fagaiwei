package io.dfjinxin.modules.price.service.impl;

import com.aliyun.oss.common.utils.CaseInsensitiveMap;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.price.dao.PssAnalyInfoDao;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("pssAnalyInfoService")
public class PssAnalyInfoServiceImpl extends ServiceImpl<PssAnalyInfoDao, PssAnalyInfoEntity> implements PssAnalyInfoService {

    @Autowired
    private PssDatasetInfoDao pssDatasetInfoDao;
    @Autowired
    private PssAnalyInfoDao pssAnalyInfoDao;

    @Override
    public PssAnalyInfoDto saveOrUpdate(PssAnalyInfoDto dto) {
        PssAnalyInfoEntity entity = PssAnalyInfoEntity.toEntity(dto);

        if (dto.getAnalyId() == null) {
            Map map = new CaseInsensitiveMap();
            map.put("analyName", dto.getAnalyName());
            map.put("analyWay", dto.getAnalyWay());
            map.put("datasetId", dto.getDataSetId());
            map.put("remarks", dto.getRemarks());
            List<PssAnalyInfoEntity> list = getAnalyInfo(map);
            if (!list.isEmpty()) {
                entity.setAnalyId(list.get(0).getAnalyId());
                dto.setAnalyId(list.get(0).getAnalyId());
            }
        }
        super.saveOrUpdate(entity);

        dto.setAnalyId(entity.getAnalyId());
        return PssAnalyInfoEntity.toData(entity);
    }

//    static Map keyTransMap = new CaseInsensitiveMap<String>() {
//        {
//            put("analyName", "analy_Name");
//            put("analyId", "analy_Id");
//            put("bussType", "buss_Type");
//            put("dataSetId", "data_Set_Id");
//            put("analyWay", "analy_way");
//        }
//    };

    @Override
    public List<PssAnalyInfoEntity> getAnalyWayByBussType(Integer bussType) {
        if (bussType == null) {
            return new ArrayList<>();
        }
        QueryWrapper<PssAnalyInfoEntity> where = new QueryWrapper();
        where.select("analy_way");
        where.eq("buss_type", bussType);
        where.groupBy("analy_way");
        return baseMapper.selectList(where);
    }

    @Override
    public List<PssAnalyInfoEntity> getAnalyInfo(Map<String, Object> params) {
        return pssAnalyInfoDao.getAnalyInfo(params);
    }
}
