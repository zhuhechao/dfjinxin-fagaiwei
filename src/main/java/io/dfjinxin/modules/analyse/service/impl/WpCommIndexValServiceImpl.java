package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.KpiInfoDto;
import io.dfjinxin.common.utils.KpiTypeEnum;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;


@Service("WpBaseIndexValService")
public class WpCommIndexValServiceImpl extends ServiceImpl<WpBaseIndexValDao, WpBaseIndexValEntity> implements WpBaseIndexValService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;
    @Autowired
    private WpBaseIndexValDao wpBaseIndexValDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpBaseIndexValEntity> page = this.page(
                new Query<WpBaseIndexValEntity>().getPage(params),
                new QueryWrapper<WpBaseIndexValEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @Desc: 二级页面(商品总览)-根据3类商品统计指定 指标类型&时间的规格品取值
     * @Param: [params]
     * @Return: io.dfjinxin.common.utils.PageUtils
     * @Author: z.h.c
     * @Date: 2019/11/13 13:54
     */
    @Override
    public PageUtils queryPageByDate(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPageByDate(page, params);
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
            if (1 == entity.getCommId()) {
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
            return wpBaseIndexValDao.queryIndexTypePrice(condition);
        }
        return wpBaseIndexValDao.queryIndexTypeByCondition(condition);
    }

    @Override
    public List<Map<String, Object>> queryIndexTypeByCommId(Integer commId) {
        return wpBaseIndexValDao.queryIndexTypeByCommId(commId);
    }

//    @Override
//    public List<Map<String, Object>> queryLevel4CommInfo(Integer commId) {
//
//        //校验商品是否存在
//        QueryWrapper where = new QueryWrapper();
//        where.eq("data_flag", 0);
//        where.eq("comm_id", commId);
//        PssCommTotalEntity commLevel2 = pssCommTotalDao.selectOne(where);
//        if (commLevel2 == null) {
//            return null;
//        }
//        //获取该3类商品下的所有4类商品
//        QueryWrapper where1 = new QueryWrapper();
//        where1.eq("data_flag", 0);
//        where1.eq("parent_code", commId);
//        List<PssCommTotalEntity> commLevel3 = pssCommTotalDao.selectList(where1);
//        if (commLevel3 == null) {
//            return null;
//        }
//        List resultList = new ArrayList();
//        //查询4类商品所有指标类型
//        String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
//        QueryWrapper where2 = new QueryWrapper();
////        where2.eq("index_flag", 0);
//        where2.inSql("comm_id", sql);
//        where2.groupBy("index_type");
//        List<WpBaseIndexValEntity> baseIndexValEntityList = wpBaseIndexValDao.selectList(where2);
//
//        Set<String> indexTypeList = new HashSet<>();
//        for (WpBaseIndexValEntity entity : baseIndexValEntityList) {
//            indexTypeList.add(entity.getIndexType());
//        }
//
//        for (String type : indexTypeList) {
//            QueryWrapper where3 = new QueryWrapper();
////            where3.eq("index_flag", 0);
//            where3.inSql("comm_id", sql);
//            where3.eq("index_type", type);
//            List<WpBaseIndexValEntity> baseIndexInfoEntities = wpBaseIndexValDao.selectList(where3);
//            KpiTypeEnum typeEnum = KpiTypeEnum.getbyType(type);
//            switch (typeEnum) {
//                //价格指标
//                case Pri:
//                    List<WpBaseIndexValEntity> priList = null;
//                    for (PssCommTotalEntity entity : commLevel3) {
//                        if (commLevel2.getCommName().equals(entity.getCommName())) {
//                            priList = doIndexPriceInfo(baseIndexInfoEntities, entity.getCommId(), type);
//                        }
//                    }
//                    Map priMap = new HashMap();
//                    priMap.put(type, priList);
//                    resultList.add(priMap);
//                    continue;
//                case Ccl:
//                    List<KpiInfoDto> cclList = doIndexInfo(baseIndexInfoEntities);
//                    Map cclMap = new HashMap();
//                    cclMap.put(type, cclList);
//                    resultList.add(cclMap);
//                    continue;
//                case Csp:
//                    List<KpiInfoDto> cspList = doIndexInfo(baseIndexInfoEntities);
//                    Map cspMap = new HashMap();
//                    cspMap.put(type, cspList);
//                    resultList.add(cspMap);
//                    continue;
//                case Cst:
//                    List<KpiInfoDto> cstList = doIndexInfo(baseIndexInfoEntities);
//                    Map cstMap = new HashMap();
//                    cstMap.put(type, cstList);
//                    resultList.add(cstMap);
//                    continue;
//                case Prd:
//                    List<KpiInfoDto> prdList = doIndexInfo(baseIndexInfoEntities);
//                    Map<String, List<KpiInfoDto>> prdMap = new HashMap();
//                    prdMap.put(type, prdList);
//                    resultList.add(prdMap);
//                    continue;
//                    //贸易
//                case Trd:
//                    List<KpiInfoDto> trdList = doIndexInfo(baseIndexInfoEntities);
//                    Map<String, List<KpiInfoDto>> trdMap = new HashMap();
//                    trdMap.put(type, trdList);
//                    resultList.add(trdMap);
//                    continue;
//                default:
//            }
//        }
//        return resultList;
//    }

    @Override
    public Map<String, Object> analyseType4CommIndexs(Integer commId) {
        //商品信息校验
        QueryWrapper where = new QueryWrapper();
        where.eq("data_flag", 0);
        where.eq("comm_id", commId);
        PssCommTotalEntity type3Comm = pssCommTotalDao.selectOne(where);
        if (type3Comm == null) {
            return null;
        }
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("data_flag", 0);
        where1.eq("parent_code", commId);
        List<PssCommTotalEntity> type4comms = pssCommTotalDao.selectList(where1);
        if (type4comms == null) {
            return null;
        }

        //统计该3类商品下所有4类商品的指标类型为价格的所有最新价格及上一天的价格作涨辐
        Map<String, Object> resMap = new HashMap<>();
        List<List<WpBaseIndexValEntity>> priceList = new ArrayList<>();
        List<List<WpBaseIndexValEntity>> noPriceList = new ArrayList<>();
        List<List<WpBaseIndexValEntity>> mapValList = new ArrayList<>();
        for (PssCommTotalEntity comm : type4comms) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", comm.getCommId());
            where2.groupBy("index_type");
            List<WpBaseIndexValEntity> indexTypeList = wpBaseIndexValDao.selectList(where2);
            for (WpBaseIndexValEntity type : indexTypeList) {
                //计算价格指标类型
                QueryWrapper where3 = new QueryWrapper();
                where3.eq("data_flag", 0);
                if (type.getIndexType().equals("价格")) {
                    //统计价格类型的4类最高指标价格
                    List<WpBaseIndexValEntity> valList = wpBaseIndexValDao.queryByIndexType(comm.getCommId(), "价格");
                    if (valList != null && valList.size() > 0) {

                        where3.eq("comm_id", valList.get(0).getCommId());
                        PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectOne(where3);
                        for (WpBaseIndexValEntity entity : valList) {
                            entity.setCommName(commTotalEntity.getCommName() + "(" + type.getIndexType() + ")");
                        }
                        //计算同比
                        valList = converTongBi(valList);
                        priceList.add(valList);
                        //计算省份地图值
                        List<WpBaseIndexValEntity> mapValTempList = wpBaseIndexValDao.queryMapValByIndexType(comm.getCommId());
                        if (mapValTempList != null && mapValTempList.size() > 1) {
                            mapValList.add(mapValTempList);
                        }
                    }
                } else {
                    //计算非价格指标类型
                    List<WpBaseIndexValEntity> noPriceValList = wpBaseIndexValDao.queryNoPriceByIndexType(comm.getCommId(), type.getIndexType());
                    if (noPriceValList != null && noPriceValList.size() > 0) {

                        where3.eq("comm_id", noPriceValList.get(0).getCommId());
                        PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectOne(where3);
                        for (WpBaseIndexValEntity entity : noPriceValList) {
                            entity.setCommName(commTotalEntity.getCommName() + "(" + type.getIndexType() + ")");
                        }
                        //计算非价格指标类型同比
                        noPriceValList = converTongBi(noPriceValList);
                        noPriceValList.remove(1);
                        noPriceList.add(noPriceValList);
                    }
                }
            }
        }

        resMap.put("lineData", priceList);
        resMap.put("mapData", mapValList);
        resMap.put("noPriceList", noPriceList);

        //计算当前最新价格和同比
        List<WpBaseIndexValEntity> currPriceList = new ArrayList<>();
        for (int i = 0; i < priceList.size(); i++) {
            WpBaseIndexValEntity first = priceList.get(i).get(0);
            currPriceList.add(first);
        }
        resMap.put("currPrice", currPriceList);

        return resMap;
    }

    /**
     * 二级页面(商品总览) add by zhc 2019.11.11
     * 根据3级商品id 获取相应该商品所有4级商品 指标信息
     *
     * @param commId
     * @return
     */
    @Override
    public List<Map<String, Object>> secondPageIndexType(Integer commId) {
        //校验商品是否存在
        QueryWrapper where = new QueryWrapper();
        where.eq("data_flag", 0);
        where.eq("comm_id", commId);
        PssCommTotalEntity commLevel2 = pssCommTotalDao.selectOne(where);
        if (commLevel2 == null) {
            return null;
        }
        //获取该3类商品下的所有4类商品
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("data_flag", 0);
        where1.eq("parent_code", commId);
        List<PssCommTotalEntity> commLevel3 = pssCommTotalDao.selectList(where1);
        if (commLevel3 == null) {
            return null;
        }
        //查询该3类商品下4类商品所有指标类型
        String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        QueryWrapper where2 = new QueryWrapper();
        where2.inSql("comm_id", sql);
        where2.groupBy("index_type");
        List<WpBaseIndexValEntity> baseIndexValEntityList = wpBaseIndexValDao.selectList(where2);

        Set<String> indexTypeList = new HashSet<>();
        for (WpBaseIndexValEntity entity : baseIndexValEntityList) {
            indexTypeList.add(entity.getIndexType());
        }

        List resultList = new ArrayList();
        for (String type : indexTypeList) {
            QueryWrapper where3 = new QueryWrapper();
            where3.inSql("comm_id", sql);
            where3.eq("index_type", type);
            where3.groupBy("comm_id");
            List<WpBaseIndexValEntity> baseIndexInfoEntities = wpBaseIndexValDao.selectList(where3);
            KpiTypeEnum typeEnum = KpiTypeEnum.getbyType(type);
            switch (typeEnum) {
                case Pri://价格
                    List<WpBaseIndexValEntity> priList = null;
                    for (PssCommTotalEntity entity : commLevel3) {
                        if (commLevel2.getCommName().equals(entity.getCommName())) {
                            priList = doIndexInfo(baseIndexInfoEntities, type);
                        }
                    }
                    Map priMap = new HashMap();
                    priMap.put(type, priList);
                    resultList.add(priMap);
                    continue;
                case Ccl://流通
                    List<KpiInfoDto> cclList = doIndexInfo(baseIndexInfoEntities, type);
                    Map cclMap = new HashMap();
                    cclMap.put(type, cclList);
                    resultList.add(cclMap);
                    continue;
                case Csp://消费
                    List<KpiInfoDto> cspList = doIndexInfo(baseIndexInfoEntities, type);
                    Map cspMap = new HashMap();
                    cspMap.put(type, cspList);
                    resultList.add(cspMap);
                    continue;
                case Cst://成本
                    List<KpiInfoDto> cstList = doIndexInfo(baseIndexInfoEntities, type);
                    Map cstMap = new HashMap();
                    cstMap.put(type, cstList);
                    resultList.add(cstMap);
                    continue;
                case Prd://生产
                    List<KpiInfoDto> prdList = doIndexInfo(baseIndexInfoEntities, type);
                    Map<String, List<KpiInfoDto>> prdMap = new HashMap();
                    prdMap.put(type, prdList);
                    resultList.add(prdMap);
                    continue;
                    //贸易
                case Trd:
                    List<KpiInfoDto> trdList = doIndexInfo(baseIndexInfoEntities, type);
                    Map<String, List<KpiInfoDto>> trdMap = new HashMap();
                    trdMap.put(type, trdList);
                    resultList.add(trdMap);
                    continue;
                case Mtl://气象
                    List<KpiInfoDto> mtlList = doIndexInfo(baseIndexInfoEntities, type);
                    Map<String, List<KpiInfoDto>> mtlMap = new HashMap();
                    mtlMap.put(type, mtlList);
                    resultList.add(mtlMap);
                    continue;
                default:
            }
        }
        return resultList;
    }

    private List<WpBaseIndexValEntity> converTongBi(List<WpBaseIndexValEntity> valEntities) {
        WpBaseIndexValEntity first = valEntities.get(0);
        WpBaseIndexValEntity last = valEntities.get(1);
        Double firstVal = first.getValue();
        Double lastVal = last.getValue();
        Double tempVal = firstVal - lastVal;
        Double tongBi = tempVal / lastVal * 100;
        DecimalFormat df = new DecimalFormat("#.00");
        first.setTongBi(df.format(tongBi) + "%");
        valEntities.set(0, first);
        return valEntities;
    }

    /**
     * @Desc: 统计3类商品下规格品指定 指标类型 的昨天价格
     * @Param: [commId, dateStr]
     * @Return: java.util.List
     * @Author: z.h.c
     * @Date: 2019/11/12 11:15
     */
    @Override
    public List<WpBaseIndexValEntity> getprovinceLastDayMapData(Integer type3CommId, String indexType, String dateStr) {
        String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + type3CommId;
        QueryWrapper<WpBaseIndexValEntity> where2 = new QueryWrapper();
        where2.inSql("comm_id", sql);
        where2.eq("index_type", indexType);
        where2.eq("date", dateStr);
        if ("价格".equals(indexType)) {
            where2.and(wrapper -> wrapper.likeLeft("area_name", "省").or().likeLeft("area_name", "自治区"));
        }
        if ("生产".equals(indexType)) {
            where2.and(wrapper -> wrapper.like("index_name", "产量"));
        }
        where2.orderByDesc("comm_id");
        where2.orderByDesc("index_name");
        List<WpBaseIndexValEntity> list = wpBaseIndexValDao.selectList(where2);

        QueryWrapper<PssCommTotalEntity> where3 = new QueryWrapper();
        for (WpBaseIndexValEntity val : list) {
            where3.eq("comm_id", val.getCommId());
            where3.eq("data_flag", 0);
            PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectOne(where3);
            val.setCommName(commTotalEntity.getCommName());
        }
        return list;
    }

  /*  private List doIndexPriceInfo(List<WpBaseIndexValEntity> list, int commId, String indexType) {

        for (WpBaseIndexValEntity indexValEntity : list) {
            if (indexValEntity.getCommId() == commId) {
                List valList = wpBaseIndexValDao.selectListBystatAreaId(commId, indexType, indexValEntity.getIndexId());
                return valList;
            }
        }
        return null;
    }*/

    /**
     * @Desc: 查询某指标类型下规格品的价格信息
     * @Param: [commIdList, type]
     * @Return: java.util.List
     * @Author: z.h.c
     * @Date: 2019/11/11 17:47
     */
    private List doIndexInfo(List<WpBaseIndexValEntity> commIdList, final String type) {
        List<KpiInfoDto> list = new ArrayList<>();
        for (WpBaseIndexValEntity indexEntity : commIdList) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", indexEntity.getCommId());
            where2.eq("index_type", type);
            where2.orderByDesc("date");
            where2.last("limit 0,1");
            WpBaseIndexValEntity indexValEntity = wpBaseIndexValDao.selectOne(where2);
            if (indexValEntity == null) {
                continue;
            }

            PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectById(indexValEntity.getCommId());
            StringBuffer sb = new StringBuffer(commTotalEntity.getCommName());
            sb.append("-");
            sb.append(indexValEntity.getIndexName());
            sb.append("-");
            sb.append(indexValEntity.getIndexType());
            KpiInfoDto dto = new KpiInfoDto();
            dto.setIndexName(sb.toString());
            dto.setIndexVal(indexValEntity.getValue().toString());
            dto.setIndexUnit(indexValEntity.getUnit());
            dto.setDataTime(indexValEntity.getDate().toString());
            dto.setCommId(indexValEntity.getCommId());
            list.add(dto);
        }
        return list;
    }


   /* private List doIndexInfo(List<WpBaseIndexValEntity> commIdList) {

        List<KpiInfoDto> list = new ArrayList<>();
        for (WpBaseIndexValEntity indexInfoEntity : commIdList) {
//            查询4类商品的价格
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", indexInfoEntity.getIndexId());
            where2.isNotNull("data_time");
            where2.orderByDesc("data_time");
            List<WpBaseIndexValEntity> indexValEntityList = wpBaseIndexValDao.selectList(where2);

            if (null != indexValEntityList && indexValEntityList.size() > 0) {
                WpBaseIndexValEntity WpBaseIndexValEntity = indexValEntityList.get(0);
                PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectById(indexInfoEntity.getCommId());
                StringBuffer sb = new StringBuffer(commTotalEntity.getCommName());
                sb.append("-");
                sb.append(WpBaseIndexValEntity.getIndexName());
                sb.append("-");
                sb.append(WpBaseIndexValEntity.getIndexType());
                KpiInfoDto dto = new KpiInfoDto();
                dto.setIndexName(sb.toString());
                dto.setIndexVal(WpBaseIndexValEntity.getValue().toString());
                dto.setIndexUnit(WpBaseIndexValEntity.getUnit());
                dto.setDataTime(WpBaseIndexValEntity.getDate().toString());
                dto.setCommId(WpBaseIndexValEntity.getCommId());
                list.add(dto);
            }
        }
        return list;
    }*/


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

        for (PssCommTotalEntity entity1 : commLevelCode1) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("level_code", "2");
            where2.eq("data_flag", "0");
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
