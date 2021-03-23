package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexInfoDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpIndexAreaEntity;
import io.dfjinxin.modules.analyse.entity.WpIndexNameEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service("wpBaseIndexInfoService")
public class WpBaseIndexInfoServiceImpl extends ServiceImpl<WpBaseIndexInfoDao, WpBaseIndexInfoEntity> implements WpBaseIndexInfoService {

    @Override
    public List<WpBaseIndexInfoEntity> getIndexTreeByIds(String [] ids) {
        if(ids==null||ids.length<1)
            return new ArrayList<>();
        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.in("index_id", ids);
        where.orderByAsc("index_id");
        return baseMapper.selectList(where);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpBaseIndexInfoEntity> page = this.page(
                new Query<WpBaseIndexInfoEntity>().getPage(params),
                new QueryWrapper<WpBaseIndexInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpBaseIndexInfoEntity> getIndexNameByType(Integer indexId) {

        if (indexId == null) {
            return new ArrayList<>();
        }

        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.eq("comm_id", indexId);
        where.eq("index_flag", 0);
        where.eq("index_type", "价格");
        where.like("index_used", "预警");
//        List<WpBaseIndexInfoEntity> wpBaseIndexInfoEntities = baseMapper.selectList(where);
//        List<WpIndexAreaEntity> ares = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getAreaName)).map(t ->{
//            WpIndexAreaEntity areaEntity = new WpIndexAreaEntity();
//            areaEntity.setAreaId(t.getIndexId());
//            areaEntity.setAreaName(t.getAreaName());
//            return areaEntity;
//        }).collect(Collectors.toList());
//        List<WpIndexNameEntity> indexAres = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getIndexName)).map(t ->{
//            WpIndexNameEntity indexNameEntity = new WpIndexNameEntity();
//            indexNameEntity.setIndexId(t.getIndexId());
//            indexNameEntity.setIndexName(t.getIndexName());
//            return indexNameEntity;
//        }).collect(Collectors.toList());
//        WpBaseIndexInfoEntity dt = new WpBaseIndexInfoEntity();
//        dt.setIndexId(indexId);
//        dt.setIndexNames(indexAres);
//        dt.setAreaNames(ares);
//        ArrayList<WpBaseIndexInfoEntity> res = new ArrayList<>();
//        res.add(dt);
        return baseMapper.selectList(where) ;
    }

    @Override
    public List<WpBaseIndexInfoEntity> getIndexNameByInfo(Integer commId, String indexName, String areaName) {
        if (commId == null) {
            return new ArrayList<>();
        }
        WpBaseIndexInfoEntity res = new WpBaseIndexInfoEntity();
        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.eq("comm_id", commId);
        where.eq("index_flag", 0);
        where.eq("index_type", "价格");
        where.like("index_used", "预警");
        if(StringUtils.isEmpty(indexName) && StringUtils.isEmpty(areaName)){
        List<WpBaseIndexInfoEntity> wpBaseIndexInfoEntities = baseMapper.selectList(where);
            List<WpIndexAreaEntity> ares = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getAreaName)).map(t ->{
                WpIndexAreaEntity areaEntity = new WpIndexAreaEntity();
                areaEntity.setAreaId(t.getIndexId());
                areaEntity.setAreaName(t.getAreaName());
                return areaEntity;
            }).collect(Collectors.toList());
            List<WpIndexNameEntity> indexAres = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getIndexName)).map(t ->{
                WpIndexNameEntity indexNameEntity = new WpIndexNameEntity();
                indexNameEntity.setIndexId(t.getIndexId());
                indexNameEntity.setIndexName(t.getIndexName());
                return indexNameEntity;
            }).collect(Collectors.toList());
        res.setCommId(commId);
        res.setAreaNames(ares);
        res.setIndexFlag(0);
        res.setIndexType("价格");
        res.setIndexUsed("预警");
        res.setIndexNames(indexAres);
        wpBaseIndexInfoEntities.clear();
        wpBaseIndexInfoEntities.add(res);
        return  wpBaseIndexInfoEntities;

        }else if(!StringUtils.isEmpty(indexName) && StringUtils.isEmpty(areaName)){
            where.eq("index_name",indexName);
            List<WpBaseIndexInfoEntity> wpBaseIndexInfoEntities = baseMapper.selectList(where);
            List<WpIndexAreaEntity> ares = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getAreaName)).map(t ->{
                WpIndexAreaEntity areaEntity = new WpIndexAreaEntity();
                areaEntity.setAreaId(t.getIndexId());
                areaEntity.setAreaName(t.getAreaName());
                return areaEntity;
            }).collect(Collectors.toList());
            List<WpIndexNameEntity> indexAres = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getIndexName)).map(t ->{
                WpIndexNameEntity indexNameEntity = new WpIndexNameEntity();
                indexNameEntity.setIndexId(t.getIndexId());
                indexNameEntity.setIndexName(t.getIndexName());
                return indexNameEntity;
            }).collect(Collectors.toList());
            res.setCommId(commId);
            res.setAreaNames(ares);
            res.setIndexNames(indexAres);
            res.setIndexFlag(0);
            res.setIndexType("价格");
            res.setIndexUsed("预警");
            wpBaseIndexInfoEntities.clear();
            wpBaseIndexInfoEntities.add(res);
            return  wpBaseIndexInfoEntities;
        }else if(!StringUtils.isEmpty(areaName) && StringUtils.isEmpty(indexName) ){
            where.eq("area_name",areaName);
            List<WpBaseIndexInfoEntity> wpBaseIndexInfoEntities = baseMapper.selectList(where);
            List<WpIndexAreaEntity> ares = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getAreaName)).map(t ->{
                WpIndexAreaEntity areaEntity = new WpIndexAreaEntity();
                areaEntity.setAreaId(t.getIndexId());
                areaEntity.setAreaName(t.getAreaName());
                return areaEntity;
            }).collect(Collectors.toList());
            List<WpIndexNameEntity> indexAres = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getIndexName)).map(t ->{
                WpIndexNameEntity indexNameEntity = new WpIndexNameEntity();
                indexNameEntity.setIndexId(t.getIndexId());
                indexNameEntity.setIndexName(t.getIndexName());
                return indexNameEntity;
            }).collect(Collectors.toList());
            res.setCommId(commId);
            res.setIndexFlag(0);
            res.setIndexType("价格");
            res.setIndexUsed("预警");
            res.setAreaNames(ares);
            res.setIndexNames(indexAres);
            wpBaseIndexInfoEntities.clear();
            wpBaseIndexInfoEntities.add(res);
            return  wpBaseIndexInfoEntities;
        }else {
            where.eq("area_name",areaName);
            where.eq("index_name",indexName);
            List<WpBaseIndexInfoEntity> wpBaseIndexInfoEntities = baseMapper.selectList(where);
            List<WpIndexAreaEntity> ares = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getAreaName)).map(t ->{
                WpIndexAreaEntity areaEntity = new WpIndexAreaEntity();
                areaEntity.setAreaId(t.getIndexId());
                areaEntity.setAreaName(t.getAreaName());
                return areaEntity;
            }).collect(Collectors.toList());
            List<WpIndexNameEntity> indexAres = wpBaseIndexInfoEntities.stream().filter(distinctByKey(WpBaseIndexInfoEntity::getIndexName)).map(t ->{
                WpIndexNameEntity indexNameEntity = new WpIndexNameEntity();
                indexNameEntity.setIndexId(t.getIndexId());
                indexNameEntity.setIndexName(t.getIndexName());
                return indexNameEntity;
            }).collect(Collectors.toList());
            res.setCommId(commId);
            res.setAreaNames(ares);
            res.setIndexFlag(0);
            res.setIndexType("价格");
            res.setIndexUsed("预警");
            res.setIndexNames(indexAres);
            wpBaseIndexInfoEntities.clear();
            wpBaseIndexInfoEntities.add(res);
            return  wpBaseIndexInfoEntities;
        }

    }

    @Override
    public List<WpBaseIndexInfoEntity> getIndexTreeByCommId(Integer commId) {
        if (commId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.eq("comm_id", commId);
        where.groupBy("index_type");

        List<WpBaseIndexInfoEntity> indexTypeList = baseMapper.selectList(where);
        for (WpBaseIndexInfoEntity entity : indexTypeList) {
            QueryWrapper<WpBaseIndexInfoEntity> where2 = new QueryWrapper<>();
            where2.eq("comm_id", entity.getCommId());
            where2.eq("index_type", entity.getIndexType());
            where2.groupBy("index_name");
            entity.setSubList(baseMapper.selectList(where2));
        }
        return indexTypeList;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        //putIfAbsent 如果值已经存在则不会放入
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
