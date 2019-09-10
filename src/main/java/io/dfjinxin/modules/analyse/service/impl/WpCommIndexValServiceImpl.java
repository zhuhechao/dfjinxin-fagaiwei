package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.KpiInfoDto;
import io.dfjinxin.common.utils.KpiTypeEnum;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexInfoDao;
import io.dfjinxin.modules.analyse.dao.WpCommIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpCommIndexValService;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("wpCommIndexValService")
public class WpCommIndexValServiceImpl extends ServiceImpl<WpCommIndexValDao, WpCommIndexValEntity> implements WpCommIndexValService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;
    @Autowired
    private WpCommIndexValDao wpCommIndexValDao;
    @Autowired
    private WpBaseIndexInfoDao wpBaseIndexInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpCommIndexValEntity> page = this.page(
                new Query<WpCommIndexValEntity>().getPage(params),
                new QueryWrapper<WpCommIndexValEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Map<String, PssCommTotalEntity>> queryList() {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        //查询0级商品
        List<PssCommTotalEntity> commType0 = pssCommTotalDao.selectList(where1);

        List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
        Map<String, PssCommTotalEntity> tempMap = new HashMap<>();
        for (PssCommTotalEntity entity : commType0) {
            Map<String, Object> temp = queryCommByLevelCode0(entity);
            PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
            if ("BC".equals(entity.getCommAbb())) {
                tempMap.put("dazong", result);
            } else {
                tempMap.put("minsheng", result);
            }
        }
        resultList.add(tempMap);
        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryDetailByCommId(Map<String, Object> condition) {
        if (condition.get("indexType").equals("价格")) {
            return wpCommIndexValDao.queryIndexTypePrice(condition);
        }
        return wpCommIndexValDao.queryIndexTypeByCondition(condition);
    }

    @Override
    public List<Map<String, Object>> queryIndexTypeByCommId(Integer commId) {
        return wpCommIndexValDao.queryIndexTypeByCommId(commId);
    }

    @Override
    public List<Map<String, Object>> queryLevel4CommInfo(Integer commId) {

        //根据3类商品查询该类下所有子商品(4级)
        List resultList = new ArrayList();
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("parent_code", commId);
        where2.eq("level_code", 3);
        where2.eq("data_flag", 0);
        List<PssCommTotalEntity> levelCode_4 = pssCommTotalDao.selectList(where2);
        for (PssCommTotalEntity commTotalEntity : levelCode_4) {
            Map kpiMap = doIndexInfo(commTotalEntity.getCommName(), getWpBaseIndexInfo(commTotalEntity.getCommId()));
            resultList.add(kpiMap);
        }

        return resultList;
    }

    private Map<String, Object> doIndexInfo(String commName, List<WpBaseIndexInfoEntity> indexInfolist) {

//        List resultList = new ArrayList();

        for (WpBaseIndexInfoEntity indexInfoEntity : indexInfolist) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", indexInfoEntity.getCommId());
            where2.eq("index_i", indexInfoEntity.getIndexId());
            where2.orderByDesc("data_time");
            List<WpCommIndexValEntity> indexValEntityList = wpCommIndexValDao.selectList(where2);
            WpCommIndexValEntity wpCommIndexValEntity = indexValEntityList.get(0);

            Map<String, Object> cclMap = new HashMap<>();
            List<KpiInfoDto> cclList = new ArrayList<>();
            StringBuffer sb = new StringBuffer(commName);
            sb.append("-");
            sb.append(wpCommIndexValEntity.getIndexName());
            sb.append("-");
            sb.append(wpCommIndexValEntity.getIndexType());
            KpiInfoDto dto = new KpiInfoDto();
            dto.setIndexName(sb.toString());
            dto.setIndexVal(wpCommIndexValEntity.getIndexVal().toString());
            dto.setIndexUnit(wpCommIndexValEntity.getIndexUnit());
            dto.setDataTime(wpCommIndexValEntity.getDataTime().toString());
            dto.setCommId(wpCommIndexValEntity.getCommId());
            KpiTypeEnum typeEnum = KpiTypeEnum.getbyType(indexInfoEntity.getIndexType());
            switch (typeEnum) {
                //价格指标
                case Pri:
                case Ccl:
                    cclList.add(dto);
                    cclMap.put(typeEnum.getVal(), cclList);
                    return cclMap;
                case Csp:
                case Cst:
            }
        }
        return null;
    }

    private List<WpBaseIndexInfoEntity> getWpBaseIndexInfo(int commId) {
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("comm_id", commId);
        where2.eq("index_flag", 0);
        return wpBaseIndexInfoDao.selectList(where2);
    }

    private Map<String, Object> queryCommByLevelCode0(PssCommTotalEntity levelCode0) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return null;
        }
        //根据0类查询1类
        QueryWrapper where1 = new QueryWrapper();
        where1.in("parent_code", levelCode0.getCommId());
        where1.eq("data_flag", "0");
        where1.eq("level_code", "1");
        // 获取一类商品
        List<PssCommTotalEntity> commLevelCode1 = pssCommTotalDao.selectList(where1);

        QueryWrapper where2 = new QueryWrapper();
        where2.eq("data_flag", "0");
        where2.eq("level_code", "2");
        for (PssCommTotalEntity entity1 : commLevelCode1) {
            where2.eq("parent_code", entity1.getCommId());
            List<PssCommTotalEntity> commLevelCode2 = pssCommTotalDao.selectList(where2);
            entity1.setSubCommList(commLevelCode2);
        }
        levelCode0.setSubCommList(commLevelCode1);
        Map<String, Object> map = new HashMap<>();
        map.put("result", levelCode0);
        return map;
    }

}
