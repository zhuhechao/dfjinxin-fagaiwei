package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.KpiInfoDto;
import io.dfjinxin.common.utils.*;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.dao.PssPriceEwarnDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.entity.WpCommPriEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import java.text.ParseException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("WpBaseIndexValService")
public class WpCommIndexValServiceImpl extends ServiceImpl<WpBaseIndexValDao, WpBaseIndexValEntity> implements WpBaseIndexValService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;
    @Autowired
    private PssCommTotalService pssCommTotalService;

    @Autowired
    private WpBaseIndexValDao wpBaseIndexValDao;

    @Autowired
    private PssPriceEwarnDao pssPriceEwarnDao;

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

    /**
     * @Desc: 二级页面(商品总览)-根据3类商品统计指定 指标类型&时间的规格品取值
     * @Param: [params]
     * @Return: io.dfjinxin.common.utils.PageUtils
     * @Author: z.h.c
     * @Date: 2019/11/13 13:54
     */
    @Override
    public List<Map<String, Object>> downloadByDate(Map<String, Object> params) {
        List<Map<String, Object>> page = super.baseMapper.downloadByDate(params);
        return page;
    }

    @Override
    public List<Map<String, Object>> getPage(Map<String, Object> params) {
        List<Map<String, Object>> page = super.baseMapper.getPage(params);
        return page;
    }

    public List<String> getDate(int size) {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -(size - i))));
        }
        return dateList;
    }

    /**
     * @Desc: 二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息
     * @Param: [params]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/29 12:30
     */
    @Override
    public Map<String, Object> queryLineChartByCondition(Map<String, Object> params) {
        Map<String, Object> resMap = new HashMap<>();
        if ((params.get("indexType").toString()).equals("价格")) {
            //获取最近有记录的时间
            String endDateStr = baseMapper.getDayBycommIdfromWpBaseIndexVal(params);
            Date endDate = new Date();
            try {
                 endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
            } catch (ParseException e) {
            }

            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(endDate), -7)));
            params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(endDate));
            List<Map<String, Object>> zhouThend = baseMapper.getJiaGeCommList(params);
            List<Map<String, Object>> zhouProThend = pssPriceEwarnDao.getThendCommList(params);
            if (zhouThend.size() > 0) {
                for (Map<String, Object> zhouList : zhouThend) {
                    params.put("id", zhouList.get("comm_id"));
                    zhouList.put("subList", baseMapper.getJiaGeIndexList(params));
                }
            }
            resMap.put("zhouThend", zhouThend);
            if (zhouProThend.size() > 0) {
                for (Map<String, Object> zhouProList : zhouProThend) {
                    params.put("id", zhouProList.get("comm_id"));
                    zhouProList.put("subList", pssPriceEwarnDao.getThendGuiCommList(params));
                }
            }
            resMap.put("zhouProThend", zhouProThend);
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(endDate), -30)));
            List<Map<String, Object>> yueThend = baseMapper.getJiaGeCommList(params);
            List<Map<String, Object>> yueProThend = pssPriceEwarnDao.getThendCommList(params);
            if (yueThend.size() > 0) {
                for (Map<String, Object> yueList : yueThend) {
                    params.put("id", yueList.get("comm_id"));
                    yueList.put("subList", baseMapper.getJiaGeIndexList(params));
                }
            }
            resMap.put("yueThend", yueThend);
            if (yueProThend.size() > 0) {
                for (Map<String, Object> yueProList : yueProThend) {
                    params.put("id", yueProList.get("comm_id"));
                    yueProList.put("subList", pssPriceEwarnDao.getThendGuiCommList(params));
                }
            }
            resMap.put("yueProThend", yueProThend);
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(endDate), -365)));
            List<Map<String, Object>> nianThend = baseMapper.getJiaGeCommList(params);
            List<Map<String, Object>> nianProThend = pssPriceEwarnDao.getThendCommList(params);
            if (nianThend.size() > 0) {
                for (Map<String, Object> nianList : nianThend) {
                    params.put("id", nianList.get("comm_id"));
                    nianList.put("subList", baseMapper.getJiaGeIndexList(params));
                }
            }
            resMap.put("nianThend", nianThend);
            if (nianProThend.size() > 0) {
                for (Map<String, Object> nianProList : nianProThend) {
                    params.put("id", nianProList.get("comm_id"));
                    nianProList.put("subList", pssPriceEwarnDao.getThendGuiCommList(params));
                }
            }
            resMap.put("nianProThend", nianProThend);
            //,地图数据-统计规格品指标类型为'价格'的各省份价格数据
            Map<String, Object> mapp = new HashMap<>();
            mapp.put("commId", params.get("commId"));
            List<Map<String, Object>> provinceLast = wpBaseIndexValDao.getCommList(mapp);
            if (provinceLast.size() > 0) {
                for (Map<String, Object> pList : provinceLast) {
                    mapp.put("commId", pList.get("comm_id"));
                    mapp.put("commDate", pList.get("date"));
                    List<Map<String, Object>> commLast = wpBaseIndexValDao.getCommProvinceList(mapp);
                    pList.put("provinceLast", commLast);
                }
            }
            resMap.put("provinceMap", provinceLast);
        } else {
            List<Map<String, Object>> zhouThend1 = baseMapper.getRiOrYueOrNianCommId(params);
            if(zhouThend1.size()>0){
                for (Map<String, Object> pList1 : zhouThend1) {
                    params.put("commId",pList1.get("comm_id"));
                    pList1.put("subList",baseMapper.getRiOrYueOrNianIndexId(params));
                }
            }
            resMap.put("zhouThend", zhouThend1);
        }
        return resMap;
    }

    @Override
    public List<Map<String, Object>> lineChartBy(Map<String, Object> params) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<String> ids = (List<String>) params.get("indexId");
        if (ids.size() > 0) {
            if (ids.size() == 1) {
                params.put("indexId", ids.get(0));
                List<Map<String, Object>> list1 = baseMapper.getRiOrYueOrNianData(params);
                if (list1.size() > 0) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> yData = new ArrayList<>();
                    List<String> xData = new ArrayList<>();
                    String indexName = list1.get(0).get("index_name").toString();
                    String indexId = list1.get(0).get("index_id").toString();
                    String unit = list1.get(0).get("unit").toString();
                    String sourceName = list1.get(0).get("source_name").toString();
                    for (Map<String, Object> l1 : list1) {
                        yData.add(l1.get("value").toString());
                        xData.add(l1.get("date").toString());
                    }
                    map1.put("indexName", indexName);
                    map1.put("unit", unit);
                    map1.put("indexId", indexId);
                    map1.put("xData", xData);
                    map1.put("yData", yData);
                    map1.put("sourceName", sourceName);
                    dataList.add(map1);
                }
            } else {
                List<String> dates = new ArrayList<>();
                List<Map<String, Object>> list3 = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (String it1 : ids) {
                    params.put("indexId", it1);
                    List<Map<String, Object>> list2 = baseMapper.getRiOrYueOrNianData(params);
                    if (list2.size() > 0) {
                        idList.add(it1);
                        for (Map<String, Object> l2 : list2) {
                            list3.add(l2);
                            dates.add(l2.get("date").toString());
                        }
                    }
                }
                Set set = new HashSet();
                List<String> newList = new ArrayList();
                if (dates.size() > 0) {
                    for (String cd : dates) {
                        if (set.add(cd)) {
                            newList.add(cd);
                        }
                    }
                    Collections.sort(newList);
                }
                if (list3.size() > 0) {
                    for (String it2 : idList) {
                        Map<String, Object> map2 = new HashMap<>();
                        List<String> yData = new ArrayList<>();
                        String indexName = "";
                        String indexId = "";
                        String unit = "";
                        String sourceName = "";
                        for (String de : newList) {
                            String val = "-";
                            for (Map<String, Object> l3 : list3) {
                                if (it2.equals(l3.get("index_id").toString()) && de.equals(l3.get("date").toString())) {
                                    val = l3.get("value").toString();
                                    indexName = l3.get("index_name").toString();
                                    indexId = l3.get("index_id").toString();
                                    unit = l3.get("unit").toString();
                                    sourceName = l3.get("source_name").toString();
                                }
                            }
                            yData.add(val);
                        }
                        map2.put("indexName", indexName);
                        map2.put("unit", unit);
                        map2.put("indexId", indexId);
                        map2.put("xData", newList);
                        map2.put("yData", yData);
                        map2.put("yData", yData);
                        map2.put("sourceName", sourceName);
                        dataList.add(map2);
                    }
                }
            }
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> linejgBy(Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("indexId");
        params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        if ("周".equals(params.get("dateType").toString())) {
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -28)));
        } else if ("月".equals(params.get("dateType").toString())) {
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -120)));
        } else {
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -730)));
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        if (ids.size() > 0) {
            if (ids.size() == 1) {
                List<Map<String, Object>> list1 = new ArrayList<>();
                params.put("indexId", ids.get(0));
                if ("1".equals(params.get("type").toString())) {
                    list1 = baseMapper.getJiaGeIndexData(params);
                } else {
                    list1 = pssPriceEwarnDao.getThendCommData(params);
                }
                if (list1.size() > 0) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> yData = new ArrayList<>();
                    List<String> xData = new ArrayList<>();
                    String indexName = list1.get(0).get("index_name").toString();
                    String indexId = list1.get(0).get("index_id").toString();
                    String unit = list1.get(0).get("unit").toString();
                    String sourceName = "";
                    if("1".equals(params.get("type").toString())){
                        sourceName = list1.get(0).get("source_name").toString();
                    }
                    for (Map<String, Object> l1 : list1) {
                        yData.add(l1.get("value").toString());
                        xData.add(l1.get("date").toString());
                    }
                    map1.put("indexName", indexName);
                    map1.put("unit", unit);
                    map1.put("indexId", indexId);
                    map1.put("xData", xData);
                    map1.put("yData", yData);
                    map1.put("sourceName", sourceName);
                    dataList.add(map1);
                }
            } else {
                List<String> dates = new ArrayList<>();
                List<Map<String, Object>> list3 = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (String it1 : ids) {
                    List<Map<String, Object>> list2 = new ArrayList<>();
                    params.put("indexId", it1);
                    if ("1".equals(params.get("type").toString())) {
                        list2 = baseMapper.getJiaGeIndexData(params);
                    } else {
                        list2 = pssPriceEwarnDao.getThendCommData(params);
                    }
                    if (list2.size() > 0) {
                        idList.add(it1);
                        for (Map<String, Object> l2 : list2) {
                            list3.add(l2);
                            dates.add(l2.get("date").toString());
                        }
                    }
                }
                Set set = new HashSet();
                List<String> newList = new ArrayList();
                if (dates.size() > 0) {
                    for (String cd : dates) {
                        if (set.add(cd)) {
                            newList.add(cd);
                        }
                    }
                    Collections.sort(newList);
                }
                if (list3.size() > 0) {
                    for (String it2 : idList) {
                        Map<String, Object> map2 = new HashMap<>();
                        List<String> yData = new ArrayList<>();
                        String indexName = "";
                        String indexId = "";
                        String unit = "";
                        String sourceName = "";
                        if("1".equals(params.get("type").toString())){
                            sourceName = list3.get(0).get("source_name").toString();
                        }
                        for (String de : newList) {
                            String val = "-";
                            for (Map<String, Object> l3 : list3) {
                                if (it2.equals(l3.get("index_id").toString()) && de.equals(l3.get("date").toString())) {
                                    val = l3.get("value").toString();
                                    indexName = l3.get("index_name").toString();
                                    indexId = l3.get("index_id").toString();
                                    unit = l3.get("unit").toString();
                                }
                            }
                            yData.add(val);
                        }
                        map2.put("indexName", indexName);
                        map2.put("unit", unit);
                        map2.put("indexId", indexId);
                        map2.put("xData", newList);
                        map2.put("yData", yData);
                        map2.put("sourceName", sourceName);
                        dataList.add(map2);
                    }
                }
            }
        }
        return dataList;
    }

    public List<Map<String, Object>> getCommItemList
            (List<String> dateList, List<Map<String, Object>> idList, Map<String, Object> params) {
        Set set = new HashSet();
        List<String> newList = new ArrayList();
        for (String cd : dateList) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        Collections.sort(newList);
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (Map<String, Object> id : idList) {
            params.put("indexId", id.get("index_id"));
            List<Map<String, Object>> zhouThend = baseMapper.getRiOrYueOrNianList(params);
            Map<String, Object> map = new HashMap();
            String unit = "";
            String indexName = "";
            List<String> yData = new ArrayList();
            if (zhouThend.size() > 0) {
                String val = "0";
                for (String cd : newList) {
                    for (Map<String, Object> dts : zhouThend) {
                        if (cd.equals(dts.get("date").toString())) {
                            val = dts.get("value").toString();
                            unit = dts.get("unit").toString();
                            indexName = dts.get("index_name").toString();
                        }
                    }
                    yData.add(val);
                }
            }
            map.put("xData", newList);
            map.put("indexId", id.get("index_id"));
            map.put("yData", yData);
            map.put("unit", unit);
            map.put("indexName", indexName);
            list1.add(map);
        }
        return list1;
    }

    public List<Map<String, Object>> getList(List<Map<String, Object>> list, int size, Map<
            String, Object> params, String type) {
        List<String> dateList = this.getDate(size);
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (Map<String, Object> ls : list) {
            Map<String, Object> map1 = new HashMap<>();
            params.put("commsId", ls.get("comm_id"));
            List<Map<String, Object>> ids = new ArrayList<>();
            if (type.equals("jg")) {
                ids = baseMapper.getJiaGeIndexList(params);
            } else {
                ids = pssPriceEwarnDao.getThendGuiCommList(params);
            }
            if (ids.size() > 0) {
                List<Map<String, Object>> list2 = new ArrayList<>();
                for (Map<String, Object> id : ids) {
                    Map<String, Object> map2 = new HashMap<>();
                    List<String> yData = new ArrayList<>();
                    String indexName = "";
                    String indexId = "";
                    String unit = "";
                    params.put("indexId", id.get("index_id"));
                    List<Map<String, Object>> dList = new ArrayList<>();
                    if (type.equals("jg")) {
                        dList = baseMapper.getJiaGeIndexData(params);
                    } else {
                        dList = pssPriceEwarnDao.getThendCommData(params);
                    }
                    if (dateList.size() > 0) {
                        for (String de : dateList) {
                            String val = "0";
                            for (Map<String, Object> dt : dList) {
                                if (de.equals(dt.get("date").toString())) {
                                    val = dt.get("value").toString();
                                }
                            }
                            indexName = dList.get(0).get("index_name").toString();
                            indexId = dList.get(0).get("index_id").toString();
                            unit = dList.get(0).get("unit").toString();
                            yData.add(val);
                        }
                        map2.put("xData", dateList);
                        map2.put("yData", yData);
                        map2.put("indexName", indexName);
                        map2.put("indexId", indexId);
                        map2.put("unit", unit);
                        list2.add(map2);
                    }
                }
                if (list2.size() > 0) {
                    map1.put("list", list2);
                    map1.put("commId", ls.get("comm_id"));
                    map1.put("commName", ls.get("comm_name"));
                    list1.add(map1);
                }
            }
        }
        return list1;
    }


//    /**
//     * @Desc: 二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息
//     * @Param: [params]
//     * @Return: java.util.Map<java.lang.String, java.lang.Object>
//     * @Author: z.h.c
//     * @Date: 2019/11/29 12:30
//     */
//    @Override
//    public Map<String, Object> queryLineChartByCondition(Map<String, Object> params) {
//
//        PssCommTotalEntity comm = pssCommTotalService.queryComm(params.containsKey("commId") ? (Integer) params.get("commId") : null);
//        String indexType = params.containsKey("indexType") ? (String) params.get("indexType") : null;
//        Integer indexId = params.containsKey("indexId") ? (Integer) params.get("indexId") : null;
//        if (comm == null || indexType == null || indexId == null) return null;
//
//        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
//        String lastMonthDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
//        String startDate = params.containsKey("startDate") ? (String) params.get("startDate") : lastMonthDayStr;
//        String endDate = params.containsKey("endDate") ? (String) params.get("endDate") : lastDayStr;
//
//        QueryWrapper where5 = new QueryWrapper();
//        where5.select("frequence");
//        where5.eq("comm_id", comm.getCommId());
//        where5.eq("index_type", indexType);
//        where5.eq("index_id", indexId);
//        where5.between("date", startDate, endDate);
//        where5.groupBy("frequence");
//        List<WpBaseIndexValEntity> frequenceList = wpBaseIndexValDao.selectList(where5);
//
//        QueryWrapper where = new QueryWrapper();
//        where.select("area_name");
//        where.eq("comm_id", comm.getCommId());
//        where.eq("index_id", indexId);
//        where.eq("index_type", indexType);
//        where.between("date", startDate, endDate);
//
//        QueryWrapper where2 = new QueryWrapper();
//        where2.eq("comm_id", comm.getCommId());
//        where2.eq("index_type", indexType);
//        where2.eq("index_id", indexId);
//        where2.between("date", startDate, endDate);
//
//        Map<String, Object> resMap = new HashMap<>();
//        frequenceList.forEach(frequence -> {
//            Map<String, Object> frequenceMap = new HashMap<>();
//            where.eq("frequence", frequence.getFrequence());
//            where.groupBy("area_name");
//            List<WpBaseIndexValEntity> areaNameList = wpBaseIndexValDao.selectList(where);
//            areaNameList.forEach(areaName -> {
//                Map<String, Object> areaNameMap = new HashMap<>();
//                where2.eq("frequence", frequence.getFrequence());
//                where2.eq("area_name", areaName.getAreaName());
//                where2.orderByAsc("date");
//                List<WpBaseIndexValEntity> valEntityList = wpBaseIndexValDao.selectList(where2);
//                valEntityList.forEach(entity -> entity.setCommName(comm.getCommName()));
//                areaNameMap.put(areaName.getAreaName(), valEntityList);
//                frequenceMap.put(frequence.getFrequence(), areaNameMap);
//            });
//            resMap.putAll(frequenceMap);
//        });
//        return resMap;
//    }

    /**
     * @Desc: 统计3类品下有哪些指标类型是价格的规格品
     * @Param: [commId]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssCommTotalEntity>
     * @Author: z.h.c
     * @Date: 2019/11/29 16:42
     */
    @Override
    public List<PssCommTotalEntity> queryCommListByCommId(Integer commId, String indexType) {
        return wpBaseIndexValDao.queryCommListByCommId(commId, indexType);
    }

    /**
     * @Desc: 根据规格品、指标类型 获取昨天各省份数据
     * @Param: [commId, indexType]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssCommTotalEntity>
     * @Author: z.h.c
     * @Date: 2019/11/29 18:12
     */
    @Override
    public List<WpBaseIndexValEntity> getProvinceMapByCommId(Integer commId, String indexType, String date) {
        if (StringUtils.isEmpty(date)) {
            date = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
        }
        return wpBaseIndexValDao.getProvinceMapByCommId(commId, date, indexType);
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
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//前一天时间

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
                    List<WpBaseIndexValEntity> valList = wpBaseIndexValDao.queryByIndexType(comm.getCommId(), "价格", lastDayStr);
                    if (!valList.isEmpty()) {

                        where3.eq("comm_id", valList.get(0).getCommId());
                        PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectOne(where3);
                        for (WpBaseIndexValEntity entity : valList) {
                            entity.setCommName(commTotalEntity.getCommName() + "(" + type.getIndexType() + ")");
                        }
                        //计算同比
                        valList = converTongBi(valList);
                        valList = converHuanBi(valList);
                        priceList.add(valList);
                        //计算省份地图值
                        List<WpBaseIndexValEntity> mapValTempList = wpBaseIndexValDao.queryMapValByIndexType(comm.getCommId(), lastDayStr);
                        if (!mapValTempList.isEmpty()) {
                            mapValList.add(mapValTempList);
                        }
                    }
                } else {
                    //计算非价格指标类型
                    List<WpBaseIndexValEntity> noPriceValList = wpBaseIndexValDao.queryNoPriceByIndexType(comm.getCommId(), type.getIndexType());
                    if (!noPriceValList.isEmpty()) {

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
            return new ArrayList<>();
        }
        //获取该3类商品下的所有4类商品
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("data_flag", 0);
        where1.eq("parent_code", commId);
        List<PssCommTotalEntity> commLevel3 = pssCommTotalDao.selectList(where1);
        if (commLevel3 == null) {
            return new ArrayList<>();
        }
        //查询该3类商品下4类商品所有指标类型
        String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
        QueryWrapper where2 = new QueryWrapper();
        where2.inSql("comm_id", sql);
        where2.eq("date", lastDayStr);
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
            where3.eq("date", lastDayStr);
            where3.groupBy("comm_id");
            List<WpBaseIndexValEntity> baseIndexInfoEntities = wpBaseIndexValDao.selectList(where3);
            KpiTypeEnum typeEnum = KpiTypeEnum.getbyType(type);
            switch (typeEnum) {
                case Pri://价格
                    List<WpBaseIndexValEntity> priList = null;
                    for (PssCommTotalEntity entity : commLevel3) {
                        if (commLevel2.getCommName().equals(entity.getCommName())) {
                            priList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                        }
                    }
                    Map priMap = new HashMap();
                    priMap.put(type, priList);
                    resultList.add(priMap);
                    break;
                case Ccl://流通
                    List<KpiInfoDto> cclList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map cclMap = new HashMap();
                    cclMap.put(type, cclList);
                    resultList.add(cclMap);
                    break;
                case Csp://消费
                    List<KpiInfoDto> cspList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map cspMap = new HashMap();
                    cspMap.put(type, cspList);
                    resultList.add(cspMap);
                    break;
                case Cst://成本收益
                    List<KpiInfoDto> cstList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map cstMap = new HashMap();
                    cstMap.put(type, cstList);
                    resultList.add(cstMap);
                    break;
                case Prd://生产
                    List<KpiInfoDto> prdList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map<String, List<KpiInfoDto>> prdMap = new HashMap();
                    prdMap.put(type, prdList);
                    resultList.add(prdMap);
                    break;
                //贸易
                case Trd:
                    List<KpiInfoDto> trdList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map<String, List<KpiInfoDto>> trdMap = new HashMap();
                    trdMap.put(type, trdList);
                    resultList.add(trdMap);
                    break;
                case Mtl://气象
                    List<KpiInfoDto> mtlList = doIndexInfo(baseIndexInfoEntities, type, lastDayStr);
                    Map<String, List<KpiInfoDto>> mtlMap = new HashMap();
                    mtlMap.put(type, mtlList);
                    resultList.add(mtlMap);
                    break;
                default:
            }
        }
        return resultList;
    }

    /**
     * @Desc: 根据频度计算环比
     * @Param: [valList]
     * @Return: java.util.List<io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity>
     * @Author: z.h.c
     * @Date: 2019/12/16 16:47
     */
    private List<WpBaseIndexValEntity> converHuanBi(List<WpBaseIndexValEntity> valList) {
        if (valList.isEmpty()) return new ArrayList<>();

        //昨天最新价格
        WpBaseIndexValEntity lastDayPrice = valList.get(0);
        String frequence = lastDayPrice.getFrequence();
        Double firstVal = lastDayPrice.getValue();
        Double lastVal = null;
        String queryDate = "";

        QueryWrapper<WpBaseIndexValEntity> where = new QueryWrapper<>();
        where.eq("comm_id", lastDayPrice.getCommId());
        where.eq("index_id", lastDayPrice.getIndexId());
        where.eq("index_type", lastDayPrice.getIndexType());
        where.eq("frequence", frequence);
        where.last("limit 0,1");
        if ("日".equals(frequence)) {
            //前天价格
            WpBaseIndexValEntity last2DayPrice = valList.get(1);
            lastVal = last2DayPrice == null ? 0 : last2DayPrice.getValue();
        }
        if ("月".equals(frequence)) {
            //统计一个月前价格
            queryDate = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
            where.eq("date", queryDate);
            WpBaseIndexValEntity lastMonthPrice = baseMapper.selectOne(where);
            lastVal = lastMonthPrice == null ? 0 : lastMonthPrice.getValue();
        }
        if ("周".equals(frequence)) {
            //统计一个月前价格
            queryDate = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -7));//一周时间
            where.eq("date", queryDate);
            WpBaseIndexValEntity lastWeekPrice = baseMapper.selectOne(where);
            lastVal = lastWeekPrice == null ? 0 : lastWeekPrice.getValue();
        }
        if ("年".equals(frequence)) {
            //统计一个月前价格
            queryDate = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -365));//一年前时间
            where.eq("date", queryDate);
            WpBaseIndexValEntity lastYearPrice = baseMapper.selectOne(where);
            lastVal = lastYearPrice == null ? 0 : lastYearPrice.getValue();
        }

        if (lastVal == 0) {
            lastDayPrice.setHuanBi("0%");
            valList.set(0, lastDayPrice);
        } else {
            Double tempVal = firstVal - lastVal;
            Double huanBi = tempVal / lastVal * 100;
            DecimalFormat df = new DecimalFormat("#.00");
            lastDayPrice.setHuanBi(df.format(huanBi) + "%");
            valList.set(0, lastDayPrice);
        }
        return valList;
    }

    /**
     * @Desc: 计算同比
     * @Param: [valEntities]
     * @Return: java.util.List<io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity>
     * @Author: z.h.c
     * @Date: 2019/12/16 16:47
     */
    private List<WpBaseIndexValEntity> converTongBi(List<WpBaseIndexValEntity> valEntities) {
        WpBaseIndexValEntity first = valEntities.get(0);
        WpBaseIndexValEntity last = valEntities.get(1);
        Double firstVal = first.getValue();
        Double lastVal = last.getValue();
        if (lastVal == 0) {
            first.setTongBi("0%");
            valEntities.set(0, first);
        } else {
            Double tempVal = firstVal - lastVal;
            Double tongBi = tempVal / lastVal * 100;
            DecimalFormat df = new DecimalFormat("#.00");
            DecimalFormat df2 = new DecimalFormat("0.00");
            if (tongBi < 0 & tongBi > -1) {
                first.setTongBi(df2.format(tongBi) + "%");
            } else if (tongBi > 0 & tongBi < 1) {
                first.setTongBi(df2.format(tongBi) + "%");
            } else {
                first.setTongBi(df.format(tongBi) + "%");
            }
            valEntities.set(0, first);
        }

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
    public List<WpBaseIndexValEntity> getprovinceLastDayMapData(Integer type3CommId, String indexType, String
            dateStr) {
        String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + type3CommId;
        QueryWrapper<WpBaseIndexValEntity> where2 = new QueryWrapper();
        where2.inSql("comm_id", sql);
        where2.eq("index_type", indexType);
        where2.eq("date", dateStr);
        if ("价格".equals(indexType)) {
            where2.and(wrapper -> wrapper.likeLeft("area_name", "省").or().likeLeft("area_name", "自治区").or().likeLeft("area_name", "市"));
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


    /**
     * @Desc: 查询某指标类型下规格品的价格信息
     * @Param: [commIdList, type]
     * @Return: java.util.List
     * @Author: z.h.c
     * @Date: 2019/11/11 17:47
     */
    private List doIndexInfo(List<WpBaseIndexValEntity> commIdList, final String type, final String date) {
        List<KpiInfoDto> list = new ArrayList<>();
        for (WpBaseIndexValEntity indexEntity : commIdList) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", indexEntity.getCommId());
            where2.eq("index_type", type);
//            where2.orderByDesc("date");
            where2.eq("date", date);
            where2.last("limit 0,1");
            WpBaseIndexValEntity indexValEntity = wpBaseIndexValDao.selectOne(where2);
            if (indexValEntity == null) {
                continue;
            }

            PssCommTotalEntity commTotalEntity = pssCommTotalDao.selectById(indexValEntity.getCommId());
            StringBuilder sb = new StringBuilder(commTotalEntity.getCommName());
            sb.append("-");
            sb.append(indexValEntity.getIndexName());
            sb.append("-");
            sb.append(indexValEntity.getIndexType());
            KpiInfoDto dto = new KpiInfoDto();
            dto.setIndexName(sb.toString());
            dto.setValue(indexValEntity.getValue().toString());
            dto.setUnit(indexValEntity.getUnit());
            dto.setDate(indexValEntity.getDate().toString());
            dto.setCommId(indexValEntity.getCommId());
            dto.setIndexId(indexValEntity.getIndexId());
            list.add(dto);
        }
        return list;
    }


    private Map<String, Object> queryCommByLevelCode0(PssCommTotalEntity levelCode0) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return new HashMap<>();
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

    @Override
    public List<WpBaseIndexValEntity> getDataByDate1(Map<String, Object> params) {
        String indexId = params.get("indexId").toString();
        Date startDate = (Date) params.get("startDate");
        QueryWrapper queryWrapper = new QueryWrapper<WpBaseIndexValEntity>();
        queryWrapper.eq("index_id", indexId);
        if (params.get("areaName") != null) {
            queryWrapper.eq("area_name", params.get("areaName"));
        }
        queryWrapper.eq("index_type", "价格");
        queryWrapper.eq("comm_id", params.get("commId"));
        queryWrapper.le("date", startDate);
        queryWrapper.gt("value", 0);
        queryWrapper.isNotNull("value");
        queryWrapper.orderByDesc(new String[]{"date"});
        queryWrapper.last(" limit 5 ");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<WpBaseIndexValEntity> getDataByDate2(Map<String, Object> params) {
        String indexId = params.get("indexId").toString();
        QueryWrapper queryWrapper = new QueryWrapper<WpBaseIndexValEntity>();
        Date endDate = (Date) params.get("endDate");
        queryWrapper.eq("index_id", indexId);
        if (params.get("areaName") != null) {
            queryWrapper.eq("area_name", params.get("areaName"));
        }
        queryWrapper.eq("index_type", "价格");
        queryWrapper.eq("comm_id", params.get("commId"));
        queryWrapper.le("date", endDate);
        queryWrapper.gt("value", 0);
        queryWrapper.isNotNull("value");
        queryWrapper.orderByDesc(new String[]{"date"});
        queryWrapper.last(" limit 1 ");
        return baseMapper.selectList(queryWrapper);
    }
}
