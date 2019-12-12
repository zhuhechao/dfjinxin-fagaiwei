package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.analyse.dao.WpMcroIndexInfoDao;
import io.dfjinxin.modules.analyse.dao.WpMcroIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpMcroIndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("wpMcroIndexInfoService")
public class WpMcroIndexInfoServiceImpl extends ServiceImpl<WpMcroIndexInfoDao, WpMcroIndexInfoEntity> implements WpMcroIndexInfoService {


    @Autowired
    private WpMcroIndexValDao wpMcroIndexValDao;

    /*@Override
    public List<Map<String, Object>> getAreaName() {
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("stat_area_id", 0);
        where1.groupBy("stat_area_name");
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("stat_area_id", 1);
        where2.groupBy("stat_area_name");
        List<WpMcroIndexValEntity> listType0 = wpMcroIndexValDao.selectList(where1);
        List<WpMcroIndexValEntity> listType1 = wpMcroIndexValDao.selectList(where2);
        Map<String, List<CountAndProvinceDto>> map = new HashMap<>();

        List<CountAndProvinceDto> dtoList0 = new ArrayList<>();
        for (WpMcroIndexValEntity type0 : listType0) {
            CountAndProvinceDto dto = new CountAndProvinceDto();
            dto.setStatAreaId(type0.getStatAreaId());
            dto.setStatAreaName(type0.getStatAreaName());
            List<Map<String, Object>> indexNameList = wpMcroIndexValDao.selectdistinctIndexName(type0.getStatAreaName(), type0.getIndexId());
            dto.setIndexNameList(indexNameList);
            dtoList0.add(dto);
        }

        List<CountAndProvinceDto> dtoList1 = new ArrayList<>();
        for (WpMcroIndexValEntity type1 : listType1) {
            CountAndProvinceDto dto = new CountAndProvinceDto();
            dto.setStatAreaId(type1.getStatAreaId());
            dto.setStatAreaName(type1.getStatAreaName());
            List<Map<String, Object>> indexNameList = wpMcroIndexValDao.selectdistinctIndexName(type1.getStatAreaName(), type1.getIndexId());
            dto.setIndexNameList(indexNameList);
            dtoList1.add(dto);
        }

        map.put("guonei", dtoList0);
        map.put("guoji", dtoList1);
        List retList = new ArrayList();
        retList.add(map);
        return retList;
    }

    @Override
    public List<WpMcroIndexValEntity> queryIndexVals(String areaName, String indexId, String dateFrom, String dateTo) {

        QueryWrapper where = new QueryWrapper();
        where.eq("index_id", indexId);
        where.eq("stat_area_name", areaName);
        if (StringUtils.isNotBlank(dateFrom) && StringUtils.isNotBlank(dateTo)) {
            where.between("data_time", dateFrom, dateTo);
        } else {
            //默认取最近三年
            where.between("data_time", DateUtils.getLastYearByVal(3), DateUtils.getCurrentDayStr());
        }
        List<WpMcroIndexValEntity> list = wpMcroIndexValDao.selectList(where);
        return list;
    }*/

    @Override
    public List<WpMcroIndexInfoEntity> getIndexTreeByType() {

        List<WpMcroIndexInfoEntity> indexTypeList = baseMapper.selectByType();
        for (WpMcroIndexInfoEntity entity : indexTypeList) {
            QueryWrapper<WpMcroIndexInfoEntity> where2 = new QueryWrapper<>();
            where2.eq("index_flag", 0);
            where2.eq("index_type", entity.getIndexType());
            where2.groupBy("index_name");
            List<WpMcroIndexInfoEntity> indexNameList = baseMapper.selectList(where2);
            entity.setSubList(indexNameList);
        }
        return indexTypeList;
    }

    @Override
    public List<WpMcroIndexInfoEntity> getIndexTreeByIds(String[] ids) {
        if (ids == null || ids.length < 1)
            return new ArrayList<>();
        QueryWrapper<WpMcroIndexInfoEntity> where = new QueryWrapper<>();
        where.in("index_id", ids);
        where.orderByAsc("index_id");
        return baseMapper.selectList(where);
    }

    /**
     * @Desc: 宏观分析-根据指标类型查询该类型下所有指标数据（wp_macro_index_val）
     * @Param: []
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/12/11 17:06
     */
    @Override
    public Map<String, Object> queryByIndexType(String indexType) {
        QueryWrapper<WpMcroIndexInfoEntity> where = new QueryWrapper();
        where.select("frequence");
        where.eq("index_flag", 0);
        where.eq("area_name", "全国");
        where.eq("index_type", indexType);
        where.groupBy("frequence");
        List<WpMcroIndexInfoEntity> frequences = baseMapper.selectList(where);

        Map<String, Object> restMap = new HashMap<>();
        frequences.forEach(frequence -> {
            QueryWrapper<WpMcroIndexInfoEntity> where2 = new QueryWrapper();
            where2.select("index_id");
            where2.eq("index_flag", 0);
            where2.eq("area_name", "全国");
            where2.eq("index_type", indexType);
            where2.eq("frequence", frequence.getFrequence());
            List<WpMcroIndexInfoEntity> indexIds = baseMapper.selectList(where2);
            Map<String, Object> indexValMap = new HashMap<>();
            indexIds.forEach(indexId -> {
                QueryWrapper<WpMcroIndexValEntity> where3 = new QueryWrapper();
                where3.eq("frequence", frequence.getFrequence());
                where3.eq("area_name", "全国");
                where3.eq("index_id", indexId.getIndexId());
                where3.orderByDesc("date");
                if ("月".equals(frequence.getFrequence())) {
                    where3.last("limit 0,12");
                }
                if ("季".equals(frequence.getFrequence())) {
                    where3.last("limit 0,4");
                }
                if ("年".equals(frequence.getFrequence())) {
                    where3.last("limit 0,12");
                }
                List<WpMcroIndexValEntity> macroValList = wpMcroIndexValDao.selectList(where3);
                if (!macroValList.isEmpty()) {
                    Collections.sort(macroValList, (o1, o2) -> {
                        if (o1.getDate().compareTo(o2.getDate()) == 0) return -1;
                        //升序排序
                        return o1.getDate().compareTo(o2.getDate());
                    });
                    indexValMap.put(macroValList.get(0).getIndexName(), macroValList);
                }
            });

            if (!indexValMap.isEmpty()) restMap.put(frequence.getFrequence(), indexValMap);

        });
        return restMap;
    }


    /**
     * @Desc: 宏观分析-查询指标类型
     * @Param: []
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/12/11 16:42
     */
    @Override
    public List<Map<String, Object>> queryIndexType() {
        QueryWrapper<WpMcroIndexInfoEntity> where = new QueryWrapper();
        where.select("index_type");
        where.eq("index_flag", 0);
        where.eq("area_name", "全国");
        where.groupBy("index_type");
        return baseMapper.selectMaps(where);
    }

}
