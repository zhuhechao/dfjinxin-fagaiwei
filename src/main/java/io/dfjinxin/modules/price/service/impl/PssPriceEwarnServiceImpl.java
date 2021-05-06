package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import io.dfjinxin.common.utils.*;
import io.dfjinxin.common.utils.echart.HttpUtil;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.dao.*;
import io.dfjinxin.modules.price.dto.AreaPrice;
import io.dfjinxin.modules.price.dto.ChinaAreaInfo;
import io.dfjinxin.modules.price.dto.CommMessage;
import io.dfjinxin.modules.price.dto.PwwPriceEwarnDto;
import io.dfjinxin.modules.price.dto.RateValDto;
import io.dfjinxin.modules.price.entity.*;
import io.dfjinxin.modules.price.service.PssCommConfService;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.dfjinxin.modules.price.service.WpUpdateInfoService;
import io.dfjinxin.modules.yuqing.TengXunYuQing;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service("pssPriceEwarnService")
public class PssPriceEwarnServiceImpl extends ServiceImpl<PssPriceEwarnDao, PssPriceEwarnEntity> implements PssPriceEwarnService {

    private static Logger logger = LoggerFactory.getLogger(PssPriceEwarnServiceImpl.class);

    @Autowired
    PssPriceEwarnDao pssPriceEwarnDao;
    @Autowired
    PssCommTotalDao pssCommTotalDao;
    @Autowired
    WpCommPriDao wpCommPriDao;
    @Autowired
    WpCommPriOrgDao wpCommPriOrgDao;

    @Autowired
    WpAsciiInfoDao wpAsciiInfoDao;

    @Autowired
    WpBaseIndexValDao wpBaseIndexValDao;

    @Autowired
    PssPriceReltDao pssPriceReltDao;

    @Autowired
    PssCommConfService pssCommConfService;

    @Autowired
    PssCommTotalService pssCommTotalService;
    @Autowired
    WpBaseIndexValService wpBaseIndexValService;

    //    @Autowired
//    private HiveService hiveService;
    @Autowired
    private WpUpdateInfoService wpUpdateInfoService;
    @Autowired
    private WpBaseIndexInfoService wpBaseIndexInfoService;

    @Value("${tengxun.path}")
    private String path;
    @Value("${tengxun.appId}")
    private String appId;
    @Value("${tengxun.pwd}")
    private String pwd;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssPriceEwarnEntity> page = this.page(
                new Query<PssPriceEwarnEntity>().getPage(params),
                new QueryWrapper<PssPriceEwarnEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @Desc: 获取3类商品所有4类商品的价格预警
     * 商品名称显示为3类，预警类型显示该4类下最高的那个
     * @Param: []
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssPriceEwarnEntity>
     * @Author: z.h.c
     * @Date: 2019/10/15 11:18
     */
    @Override
    public Map<String, Object> queryList() {

        Map<String, Object> map = firstPageView(false);
        if (map == null || !map.containsKey("ewanInfo")) {
            return null;
        }

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("ewanInfo", map.get("ewanInfo"));
        return resMap;
    }

    /**
     * @Desc: 二级页面(预警展示)
     * 统计指定3类商品下指定预警类型【常规&非常规】的规格品预警信息
     * @Param: [commId, ewarnTypeId]
     * @Return: java.util.List<java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/13 15:55
     */
    @Override
    public Map<String, Object> queryconfByewarnTypeId(Integer commId, Integer ewarnTypeId) {

        if (commId == null || ewarnTypeId == null) return null;
        List<PssCommConfEntity> entityList = pssCommConfService.queryByewarnTypeId(commId, ewarnTypeId);
        Set<Integer> commIds = new HashSet<>();
        for (PssCommConfEntity entity : entityList) {
            commIds.add(entity.getCommId());
        }

        Map<String, Object> retMap = new HashMap<>();
        for (Integer commIdOnly : commIds) {
            Set<Integer> indexIds = new HashSet<>();
            String commName = "";
            for (PssCommConfEntity entity : entityList) {
                if (commIdOnly.intValue() == entity.getCommId().intValue()) {
                    indexIds.add(entity.getIndexId());
                    commName = entity.getCommName();
                }
            }
            retMap.put(commName, indexIds);
        }
        return retMap;
    }


    /**
     * @Desc: 新的首页展示
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2019/11/16 13:56
     */
    @Override
    public Map<String, Object> indexPageViewLeft(Map<String, Object> params, boolean queryHive) {
        Map<String, Object> map = new HashMap<>();
        //获取数据总量
        List<Map<String, Object>> list1 = wpBaseIndexValDao.getDataCount();
        map.put("dataTotal", 0);
        if (queryHive) {
            int tengxunCount = getProgrammeDistribution();
            if (list1.size() > 0) {
                map.put("dataTotal", Integer.parseInt(list1.get(0).get("count").toString()) + tengxunCount);
            }
        } else {
            map.put("dataTotal", list1.get(0).get("count"));
        }
        //获取大宗和民生商品数量
        List<Map<String, Object>> list12 = pssCommTotalDao.getShopCountBycode();
        if (list12.size() > 0) {
            map.put("shopTotal", list12);
        }
        Map<String, Object> ma = new HashMap<>();
        ma.put("itrmDate",new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        //获取各预警级别商品数量
        List<Map<String, Object>> list2 = baseMapper.getCountByEwarmType(ma);
        if (list2.size() > 0) {
            for (Map<String, Object> ent2 : list2) {
                ma.put("ewarnLevel",ent2.get("ewarn_level"));
                List<Map<String, Object>> lis2 = baseMapper.getEwarmIndexList(ma);
                ent2.put("indexList",lis2);
            }
        }
        map.put("ewarmTypeTotal", list2);
        //获取各预警级别商品数量趋势
        List<Map<String, Object>> list3 = baseMapper.getCountByEwarmTypeAndDate(params);
        if (list3.size() > 0) {
            List<Map<String, Object>> l1 = new ArrayList<>();
            List<Map<String, Object>> l2 = new ArrayList<>();
            List<Map<String, Object>> l3 = new ArrayList<>();
            Map<String, Object> m1 = new HashMap<>();
            for (Map<String, Object> entity : list3) {
                if (entity.get("code_name").equals("高级预警")) {
                    l1.add(entity);
                }
                if (entity.get("code_name").equals("中级预警")) {
                    l2.add(entity);
                }
                if (entity.get("code_name").equals("低级预警")) {
                    l3.add(entity);
                }
            }
            m1.put("heightEwarm", l1);
            m1.put("intermediateEwarm", l2);
            m1.put("lowerEwarm", l3);
            map.put("ewarmMouthTotal", m1);
        } else {
            map.put("ewarmMouthTotal", null);
        }
        return map;
    }

    /**
     * @Desc: 新的首页展示
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2019/11/16 13:56
     */
    @Override
    public Map<String, Object> indexPageViewCenter() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("itrmDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        params.put("y_itrmDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -2)));
        //获取涨幅前6商品
        params.put("smaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
        params.put("emaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        List<Map<String, Object>> list1 = baseMapper.getIncreaseThree(params);

        //全国的省份以及市
        Map<String, List<ChinaAreaInfo>> listMap = handleChinaAreaInfo();

        if (list1.size() > 0) {
            for (Map<String, Object> en1 : list1) {
                String commId = String.valueOf(en1.get("comm_id"));
                List<CommMessage> commMessageByCommId = baseMapper.getCommMessageByCommId2(commId,null);
                if (commMessageByCommId.size() < listMap.size()){
                    Set<String> collect = commMessageByCommId.stream().map(CommMessage::getCommArea)
                            .collect(Collectors.toSet());
                    listMap.forEach((k, v) -> {
                        if (!collect.contains(k)) {
                            //获取没有该省份价格下的城市
                            Set<String> cityList = v.get(0).getChinaAreaInfos().stream().map(ChinaAreaInfo::getAreaName).collect(Collectors.toSet());
                            if (cityList.size() > 0){
                                List<CommMessage> commMessageByCommId2 = baseMapper.getCommMessageByCommId2(commId, cityList);
                                if (commMessageByCommId2.size() > 0){
                                    CommMessage commMessage = commMessageByCommId2.get(0);
                                    //否则设置该市的价格为该省份的价格
                                    commMessageByCommId.add(new CommMessage(commMessage.getCommName(), k, commMessage.getCommPrice(),commMessage.getCommUnit(),commMessage.getCommRange()));
                                }
                            }
                        }
                    });
                }

                Map<String, Object> yujijgMap = new HashMap<>();
                List<Map<String, Object>> li1 = new ArrayList<>();
                List<Map<String, Object>> li2 = new ArrayList<>();
                List<Map<String, Object>> li3 = new ArrayList<>();
                params.put("commId", commId);
                //获取2020-11-20到昨天的 该commId发生的高级,中级 和低级预警
                List<Map<String, Object>> lisss1 = baseMapper.getProvinceByCommId(params);
                params.put("type", 1);
                en1.put("itemProvinceList",viewMap(params));
                if (lisss1.size() > 0) {
                    List<String> areaCode = lisss1.stream().map(t -> (String)t.get("stat_area_code")).distinct().collect(Collectors.toList());
                    //获取前6的各个地区对应的省份
                    List<Map<String, String>> proArea = baseMapper.getProArea(areaCode);
                    Map<String,Object> couFlg = new HashMap<>();
                    for(Map<String, String> pr : proArea){
                        if(pr.get("areaName").equals(pr.get("contryName"))){
                            couFlg.put(pr.get("areaName"),1);
                        }
                    }
                    Iterator<Map<String, Object>> iterator = lisss1.iterator();
                    while (iterator.hasNext()){
                        Map<String, Object> pr= iterator.next();
                        for(Map<String, String> pa : proArea){
                            if(pr.get("stat_area_code").equals(pa.get("areaName")) && !pa.get("areaName").equals(pa.get("contryName")) && couFlg.containsKey(pa.get("contryName"))){
                                iterator.remove();
                            }else if(pr.get("stat_area_code").equals(pa.get("areaName")) && !pa.get("areaName").equals(pa.get("contryName"))){
                                pr.put("stat_area_code",pa.get("contryName"));
                            }
                        }
                    }
                    for (Map<String, Object> en2 : lisss1) {
                        if (en2.get("ewarn_level").equals(71)) {
                            li1.add(en2);
                        }
                        if (en2.get("ewarn_level").equals(72)) {
                            li2.add(en2);
                        }
                        if (en2.get("ewarn_level").equals(73)) {
                            li3.add(en2);
                        }
                    }
                }
                yujijgMap.put("gaoji", li1);
                yujijgMap.put("zhongji", li2);
                yujijgMap.put("diji", li3);
                yujijgMap.put("commMessage",commMessageByCommId);
                en1.put("provinceList", yujijgMap);
            }
        }
        map.put("topUpCommodity", list1);
        //获取跌幅前6商品
        List<Map<String, Object>> list2 = baseMapper.getDeclineThree(params);
        List<Map<String, Object>> listtt = new ArrayList<>();
        for (Map<String, Object> ep111 : list2) {
            boolean f = true;
            for (Map<String, Object> qq111 : list1) {
                if (ep111.get("comm_id").toString().equals(qq111.get("comm_id").toString())) {
                    f = false;
                }
            }
            if (f && listtt.size() < 6) {
                listtt.add(ep111);
            }
        }
        list2 = listtt;
        if (list2.size() > 0) {
            for (Map<String, Object> ep1 : list2) {
                Map<String, Object> yujijgMap1 = new HashMap<>();

                String commId = String.valueOf(ep1.get("comm_id"));
                List<CommMessage> commMessageByCommId = baseMapper.getCommMessageByCommId2(commId,null);

                //当返回的数据与当前中国省份的数据数量不匹配时候 , 吧当前省份里的城市当做当前省份的数据
                if (commMessageByCommId.size() < listMap.size()){
                    Set<String> collect = commMessageByCommId.stream().map(CommMessage::getCommArea)
                            .collect(Collectors.toSet());
                    listMap.forEach((k, v) -> {
                        if (!collect.contains(k)) {
                            //获取没有该省份价格下的城市
                            Set<String> cityList = v.get(0).getChinaAreaInfos().stream().map(ChinaAreaInfo::getAreaName).collect(Collectors.toSet());
                            if (cityList.size() > 0){
                                List<CommMessage> commMessageByCommId2 = baseMapper.getCommMessageByCommId2(commId, cityList);
                                if (commMessageByCommId2.size() > 0){
                                    CommMessage commMessage = commMessageByCommId2.get(0);
                                    //否则设置该市的价格为该省份的价格
                                    commMessageByCommId.add(new CommMessage(commMessage.getCommName(), k, commMessage.getCommPrice(),commMessage.getCommUnit(),commMessage.getCommRange()));
                                }
                            }
                        }
                    });
                }
                List<Map<String, Object>> lp1 = new ArrayList<>();
                List<Map<String, Object>> lp2 = new ArrayList<>();
                List<Map<String, Object>> lp3 = new ArrayList<>();
                params.put("commId", commId);
                List<Map<String, Object>> lisss2 = baseMapper.getProvinceByCommId(params);
                params.put("type", 0);
                ep1.put("itemProvinceList",viewMap(params));
                if (lisss2.size() > 0) {
                    List<String> areaCode = lisss2.stream().map(t -> (String)t.get("stat_area_code")).distinct().collect(Collectors.toList());
                    List<Map<String, String>> proArea = baseMapper.getProArea(areaCode);
                    Map<String,Object> couFlg = new HashMap<>();
                    for(Map<String, String> pr : proArea){
                        if(pr.get("areaName").equals(pr.get("contryName"))){
                            couFlg.put(pr.get("areaName"),1);
                        }
                    }
                    Iterator<Map<String, Object>> iterator = lisss2.iterator();
                    while (iterator.hasNext()){
                        Map<String, Object> pr= iterator.next();
                        for(Map<String, String> pa : proArea){
                            if(pr.get("stat_area_code").equals(pa.get("areaName")) && !pa.get("areaName").equals(pa.get("contryName")) && couFlg.containsKey(pa.get("contryName"))){
                                iterator.remove();
                            }else if(pr.get("stat_area_code").equals(pa.get("areaName")) && !pa.get("areaName").equals(pa.get("contryName"))){
                                pr.put("stat_area_code",pa.get("contryName"));
                            }
                        }
                    }
                    for (Map<String, Object> ep2 : lisss2) {
                        if (ep2.get("ewarn_level").equals(71)) {
                            lp1.add(ep2);
                        }
                        if (ep2.get("ewarn_level").equals(72)) {
                            lp2.add(ep2);
                        }
                        if (ep2.get("ewarn_level").equals(73)) {
                            lp3.add(ep2);
                        }
                    }
                }
                yujijgMap1.put("gaoji", lp1);
                yujijgMap1.put("zhongji", lp2);
                yujijgMap1.put("diji", lp3);
                yujijgMap1.put("commMessage", commMessageByCommId);

                ep1.put("provinceList", yujijgMap1);
            }
        }
        map.put("topDownCommodity", list2);

        //获取指定商品在各省份的价格信息
        List<Map<String, Object>> list3 = baseMapper.getPriceDistribution(params);
        int a = 0, b = 0, c = 0;
        //个预警等级省份数量
        if (list3.size() > 0) {
            for (Map<String, Object> entity : list3) {
                if (entity.get("stat_area_code").equals("中国") && entity.get("stat_area_code").equals("全国")) {
                    if (entity.get("code_name").equals("高级预警")) {
                        a++;
                    }
                    if (entity.get("code_name").equals("中级预警")) {
                        b++;
                    }
                    if (entity.get("code_name").equals("低级预警")) {
                        c++;
                    }
                }
            }
        }
        Map<String, Object> ma = new HashMap<>();
        ma.put("heightCount", a);
        ma.put("intermediateCount", b);
        ma.put("lowerCount", c);
        map.put("provinceCount", ma);
        map.put("provinceEwarm", list3);
        return map;
    }

    /**
     * @Desc: 新的首页查询地图数据
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2019/11/16 13:56
     */
//    @Override
    public Map<String, Object> viewMap(Map<String, Object> params) {
        params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
        params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        params.put("upRange","");
        params.put("downRange","");
        if("1".equalsIgnoreCase(params.get("type").toString())){
            params.put("upRange",1);
        }else{
            params.put("downRange",1);
        }
        // 获取 最近一个星期  该commId 各省份 涨幅度
        List<Map<String, Object>> list = baseMapper.getEwarnProvince(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        if (list.size() > 0) {
            List<String> areaCode = list.stream().map(t -> (String)t.get("stat_area_code")).distinct().collect(Collectors.toList());
            //获取 该地区 所在 的省份
            List<Map<String, String>> proArea = baseMapper.getProArea(areaCode);
            Map<String,Object> couFlg = new HashMap<>();
            for(Map<String, String> pr : proArea){
                if(pr.get("areaName").equals(pr.get("contryName"))){
                    couFlg.put(pr.get("areaName"),1);
                }
            }
            Iterator<Map<String, Object>> iterator = list.iterator();
            while (iterator.hasNext()){
                Map<String, Object> pr= iterator.next();
                if(!couFlg.containsKey(pr.get("stat_area_code"))){
                    for(Map<String, String> pa : proArea){
                        if( pr.get("stat_area_code").equals(pa.get("areaName")) && couFlg.containsKey(pa.get("contryName"))){
                            iterator.remove();
                        }
                    }
                }
            }
            for (Map<String, Object> entity : list) {
                params.put("ewarnDate",entity.get("ewarn_date"));
                params.put("ewarnProvince",entity.get("stat_area_code"));
                List<Map<String, Object>> li =  baseMapper.getEwarnProvinceInfo(params);
                Iterator<Map<String, Object>> res = li.iterator();
                while (res.hasNext()){
                    Map<String, Object> pr= res.next();
                    for(Map<String, String> pa : proArea){
                        if(pr.get("stat_area_code").equals(pa.get("areaName"))){
                            pr.put("stat_area_code",pa.get("contryName"));
                        }
                    }
                }

                if(li.size() > 0){
                    lists.addAll(li);
                }
            }
        }
        Map<String, Object> ma = new HashMap<>();
        ma.put("ewarnProvinceList",lists);
        return  ma;
    }

    /**
     * @Desc: 首页展示 第二次修改,为false不查询Hive
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/10/23 13:56
     */
    @Override
    public Map<String, Object> firstPageView(boolean queryHive) {
        Map<String, Object> retMap = new HashMap<>();
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
        List<PssPriceEwarnEntity> yestDayMaxPricEwarnList = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (int x = 1; x <= 30; x++) {
            String lastDayStr2 = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -x));
            QueryWrapper where1 = new QueryWrapper();
            where1.select("comm_id", lastDayStr2);
            where1.eq("date(ewarn_date)", lastDayStr2);
            where1.groupBy("comm_id");
            List<PssPriceEwarnEntity> priceEwarnList = pssPriceEwarnDao.selectList(where1);
            //根据分组id,查询当前日期前一天的数据
            for (PssPriceEwarnEntity entity : priceEwarnList) {
                if (!list.contains(entity.getCommId())) {
                    List<PssPriceEwarnEntity> entities = pssPriceEwarnDao.queryPriceEwarnByDate(entity.getCommId(), lastDayStr2);
                    list.add(entity.getCommId());
                    yestDayMaxPricEwarnList.add(entities.get(0));
                }
            }
        }
        List<RateValDto> rateValDtos = new ArrayList<>();
        List<PssPriceEwarnEntity> ewanInfoList = new ArrayList<>();
        for (PssPriceEwarnEntity entity : yestDayMaxPricEwarnList) {
            WpAsciiInfoEntity asciiInfoEntity = wpAsciiInfoDao.selectById(entity.getEwarnLevel());
            PssCommTotalEntity commTotalEntity = getParantCommByCommId(entity.getCommId());
            //把预警商品名称设置为父类商品名称
            if (commTotalEntity != null) {
                entity.setCommName(commTotalEntity.getCommName());
                entity.setCommId(commTotalEntity.getCommId());
                if (asciiInfoEntity != null)
                    entity.setEwarnLevel(asciiInfoEntity.getCodeName());
            }
            ewanInfoList.add(entity);
            //计算昨天涨幅&价格最高的预警类型占比
            RateValDto rateValDto = new RateValDto();
            rateValDto.setEwarnLevel(entity.getEwarnLevel());
            rateValDtos.add(rateValDto);
        }

        //统计数据总量
        if (queryHive) {
            Long tableRecordCount = wpUpdateInfoService.getEverydayInfoTotal();
            long baseCount = tableRecordCount == null ? 0 : tableRecordCount.longValue();
            int tengxunCount = getProgrammeDistribution();
            //step1,实时预览-总量(万）
            retMap.put("commTotal", baseCount + tengxunCount);

        }

        Map<String, Object> lineDateMap = new HashMap<>();
        QueryWrapper<PssPriceEwarnEntity> queryWrapper = new QueryWrapper();
        queryWrapper.select("ewarn_level");
        queryWrapper.groupBy("ewarn_level");
        List<PssPriceEwarnEntity> ewarnLevelList = pssPriceEwarnDao.selectList(queryWrapper);
        for (PssPriceEwarnEntity entity : ewarnLevelList) {
            Map<String, List<PwwPriceEwarnDto>> ewarnLevelMap = new HashMap<>();
            List<PwwPriceEwarnDto> dtoList = pssPriceEwarnDao.getEwarnCountByDate(entity.getEwarnLevel(), DateUtils.getMonthFirstDayStr(), lastDayStr);
            if (dtoList == null) continue;
            //没有预警数据时添加为0
            setDefaultDate(dtoList);
            if ("71".equals(entity.getEwarnLevel())) {
                //昨天红色预警涨数量
                ewarnLevelMap.put("高级预警", dtoList);
            }
            if ("72".equals(entity.getEwarnLevel())) {
                ewarnLevelMap.put("中级预警", dtoList);
            }
            if ("73".equals(entity.getEwarnLevel())) {
                ewarnLevelMap.put("低级预警", dtoList);
            }
//            if ("74".equals(entity.getEwarnLevel())) {
//                ewarnLevelMap.put("绿色预警", dtoList);
//            }
            lineDateMap.putAll(ewarnLevelMap);
        }
        //step2,商品预警趋势-按预警级别分组统计 从本月一号到昨天预警数据
        retMap.put("priVal", lineDateMap);

        //step3,商品预警分布-昨天涨幅&价格最高的商品预警类型占比
        retMap.put("rateVel", contionRateVal(rateValDtos));
        //step4,商品预警详情(球形)
        retMap.put("ewanInfo", distinctSameSubCommEwarn(ewanInfoList));
        //查询昨日各种预警级别的数量
        List<Map<Integer, Object>> countList = pssPriceEwarnDao.countEwarn(lastDayStr);
        Map<String, Object> countMap = new HashMap<>();
        for (Map<Integer, Object> map : countList) {
            if (map != null && !map.isEmpty()) {
                Integer key = (Integer) map.get("ewarnLevel");
                Long total = (Long) map.get("total");
                int level = key.intValue();
                int sum = total.intValue();
                if (level == 71) {
                    //昨天红色预警涨数量
                    countMap.put("redEwarnTotal", sum);
                }
                if (level == 72) {
                    countMap.put("orangeEwarnTotal", sum);
                }
                if (level == 73) {
                    countMap.put("yellowEwarnTotal", sum);
                }
//                if (level == 74) {
//                    countMap.put("greenEwarnTotal", sum);
//                }
            }
        }

        //step5,实时预览-昨日各种预警级别的数量
        Map<String, Object> temp = new HashMap<>();
        temp.put("redEwarnTotal", 0);
        temp.put("orangeEwarnTotal", 0);
        temp.put("yellowEwarnTotal", 0);
//        temp.put("greenEwarnTotal", 0);
        temp.putAll(countMap);
        retMap.putAll(temp);
        return retMap;
    }

    private void setDefaultDate(List<PwwPriceEwarnDto> set) {
        //月初一号
        String start = DateUtils.getMonthFirstDayStr();
        //昨天日期
        String end = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
        Date dStart = null;
        Date dEnd = null;
        try {
            dStart = sdf.parse(start);
            dEnd = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> dateList = DateUtils.getDates(dStart, dEnd);
        List<String> dateStrList = new ArrayList<>();
        for (Date date : dateList) {
            dateStrList.add(sdf.format(date));
        }

        Set<String> dateStrList2 = new HashSet<>();
        for (PwwPriceEwarnDto dto : set) {
            dateStrList2.add(dto.getEwarnDate());
        }

        dateStrList.removeAll(dateStrList2);
        for (String str : dateStrList) {
            PwwPriceEwarnDto dto = new PwwPriceEwarnDto();
            dto.setEwarnDate(str);
            dto.setEwarnCount(0);
            set.add(dto);
        }

        Collections.sort(set, (o1, o2) -> {
            if (o1.getEwarnDate().compareTo(o2.getEwarnDate()) == 0) return -1;
            //升序排序
            return o1.getEwarnDate().compareTo(o2.getEwarnDate());
        });
    }

    @Override
    public List<PssPriceEwarnEntity> getDayReportData(Map<String, Object> params) {

        QueryWrapper<PssPriceEwarnEntity> qw = new QueryWrapper<>();
        Date startDate = (Date) params.get("startDate");
        Date endDate = (Date) params.get("endDate");
        String commId = params.get("commId") + "";
        qw.eq("comm_id", commId);
        // qw.between("ewarn_date", startDate, endDate);
        qw.orderByAsc("ewarn_date");
        return getBaseMapper().selectList(qw);
    }

    @Override
    public List<Map<String, Object>> getDayReportDataForBarImage(Map<String, Object> params) {
        return pssPriceEwarnDao.getDayReport(params);
    }

    private Map<String, Object> distinctSameSubCommEwarn(List<PssPriceEwarnEntity> ewanInfoList) {
        //用于存放重复的元素的list
        for (int i = 0; i < ewanInfoList.size() - 1; i++) {
            for (int j = ewanInfoList.size() - 1; j > i; j--) {
                if (ewanInfoList.get(j).getCommId().equals(ewanInfoList.get(i).getCommId())) {
                    if (ewanInfoList.get(j).getPriRange().compareTo(ewanInfoList.get(i).getPriRange()) == 1) {
                        ewanInfoList.remove(i);//删除重复元素
                    } else {
                        ewanInfoList.remove(j);//删除重复元素
                    }
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        List<PssPriceEwarnEntity> dazong = new ArrayList();
        List<PssPriceEwarnEntity> minsheng = new ArrayList();
        for (PssPriceEwarnEntity entity : ewanInfoList) {
            //取到的是3级商品的id，计算该3类商品属于哪一类商品
            PssCommTotalEntity tyep1Comm = pssCommTotalDao.getType1CommBySubCommId(entity.getCommId());
            //大宗商品
            if (tyep1Comm != null && tyep1Comm.getCommId() == 1) {
                dazong.add(entity);
            } else {
                minsheng.add(entity);
            }
        }
        map.put("dazong", dazong);
        map.put("minsheng", minsheng);
        return map;
    }

    /**
     * 计算二级页面(商品总览)除指标类型信息外的其它数据
     *
     * @param commId
     * @return
     */
    @Override
    public Map<String, Object> ewarmInfo(Integer commId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        String lastMonthDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -31));//一个月前时间
        String lastYearDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -366));//去年昨天时间
        String lastYearMonthDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -397));//去年一个月前时间
        param.put("commId", commId);
        param.put("startDate", lastMonthDayStr);
        param.put("endDate", lastDayStr);
        //近一个月预警信息
        List<Map<String, Object>> list1 = baseMapper.getEwarmInfoByDate(param);
        map.put("dangqianyujing", null);
        if (list1.size() > 0) {
            map.put("dangqianyujing", list1.get(list1.size() - 1));
        }
        map.put("yueyujing", list1);
        param.put("startDate", lastYearMonthDayStr);
        param.put("endDate", lastYearDayStr);
        //去年同期近一个月预警信息
        List<Map<String, Object>> list2 = baseMapper.getEwarmInfoByDate(param);
        map.put("qunianyueyujing", list2);
        return map;
    }

    /**
     * 计算二级页面(商品总览)除指标类型信息外的其它数据
     *
     * @param commId
     * @return
     */
    @Override
    public Map<String, Object> secondPageDetail(Integer commId) {

        Map<String, Object> map = new HashMap<>();

        // step 1、计算环比、同比 无数据此内容返回null
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        String lastMonthDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
        //step2,规格品价格数据-统计昨天各规格品的商品价格数据
//        final String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        List<WpBaseIndexValEntity> lastDayValList = wpBaseIndexValDao.queryLastDayPriceByCommId(commId, lastDayStr);
        List<WpBaseIndexValEntity> lis = new ArrayList<>();
        for (WpBaseIndexValEntity dt : lastDayValList) {
            if (dt.getDate().equals(lastDayValList.get(0).getDate())) {
                lis.add(dt);
            }
        }
        map.put("priceList", lis);

//        //step3,地图数据-统计规格品指标类型为'价格'的各省份昨天价格数据
//        Map<String, Object> mapp = new HashMap<>();
//        mapp.put("commId", commId);
//        mapp.put("eDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
//        mapp.put("sDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
//        List<Map<String, Object>> provinceLast = wpBaseIndexValDao.getProvince(mapp);
//        if (provinceLast.size() > 0) {
//            for (Map<String, Object> pList : provinceLast) {
//                mapp.put("province", pList.get("area_name"));
//                mapp.put("itemDate", pList.get("date"));
//                List<Map<String, Object>> commLast = wpBaseIndexValDao.getProvinceCommList(mapp);
//                pList.put("commLast", commLast);
//            }
//        }
//        map.put("provinceMap", provinceLast);

        //进一个月商品价格走势和涨跌幅
//        Map<String, Object> ma = new HashMap<>();
////        ma.put("commId", commId);
////        ma.put("emaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
////        ma.put("smaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
////        List<Map<String, Object>> tp = baseMapper.getMaxPro(ma);
////        map.put("tongBi", 0);
////        map.put("yujing", "");
////        map.put("price", 0);
////        map.put("unit", "");
////        if (tp.size() > 0) {
////            map.put("tongBi", tp.get(0).get("pri_range"));
////            map.put("yujing", tp.get(0).get("code_name"));
////            map.put("price", tp.get(0).get("pri_value"));
////            map.put("unit", tp.get(0).get("unit"));
////        }
        Map<String, Object> ma = new HashMap<>();
        ma.put("commId", commId);
        ma.put("emaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
         ma.put("smaDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
        List<Map<String, Object>> tp = baseMapper.getMaxProCommid(ma);
        map.put("commIdList", tp);
         ma.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
        ma.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        List<Map<String, Object>> zhoujagezoushi = wpBaseIndexValDao.getIndexThendCommIds(ma);
        map.put("zhoujagezoushi", null);
        if (zhoujagezoushi.size() > 0) {
//            map.put("zhoujagezoushi",this.getList(zhoujagezoushi,7,"zhoushi"));
            for (Map<String, Object> lst1 : zhoujagezoushi) {
                ma.put("indexId", lst1.get("comm_id"));
                lst1.put("subList", wpBaseIndexValDao.getIndexThendIndexs(ma));
            }
            map.put("zhoujagezoushi", zhoujagezoushi);
        }
        ma.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -30)));
        List<Map<String, Object>> yuejagezoushi = wpBaseIndexValDao.getIndexThendCommIds(ma);
        map.put("yuejagezoushi", null);
        if (yuejagezoushi.size() > 0) {
            for (Map<String, Object> lst1 : yuejagezoushi) {
                ma.put("indexId", lst1.get("comm_id"));
                lst1.put("subList", wpBaseIndexValDao.getIndexThendIndexs(ma));
            }
            map.put("yuejagezoushi", yuejagezoushi);
        }
        ma.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -365)));
        List<Map<String, Object>> nianjagezoushi = wpBaseIndexValDao.getIndexThendCommIds(ma);
        map.put("nianjagezoushi", null);
        if (nianjagezoushi.size() > 0) {
            for (Map<String, Object> lst1 : nianjagezoushi) {
                ma.put("indexId", lst1.get("comm_id"));
                lst1.put("subList", wpBaseIndexValDao.getIndexThendIndexs(ma));
            }
            map.put("nianjagezoushi", nianjagezoushi);
        }
        //未来一个月商品预测价格走势
        ma.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), 0)));
        ma.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), +6)));
        List<Map<String, Object>> zhouyucejagezoushi = baseMapper.getForePriceThendCommIds(ma);
        map.put("zhouyucejagezoushi", null);
        if (zhouyucejagezoushi.size() > 0) {
            for (Map<String, Object> lst1 : zhouyucejagezoushi) {
                ma.put("indexId", lst1.get("comm_id"));
                lst1.put("subList", baseMapper.getForePriceThendIndexs(ma));
            }
            map.put("zhouyucejagezoushi", zhouyucejagezoushi);
        }
        ma.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), +29)));
        List<Map<String, Object>> yueyucejagezoushi = baseMapper.getForePriceThendCommIds(ma);
        map.put("yueyucejagezoushi", null);
        if (yueyucejagezoushi.size() > 0) {
            for (Map<String, Object> lst1 : yueyucejagezoushi) {
                ma.put("indexId", lst1.get("comm_id"));
                lst1.put("subList", baseMapper.getForePriceThendIndexs(ma));
            }
            map.put("yueyucejagezoushi", yueyucejagezoushi);
        }

//        //step5,区域价格分布 规格品指标类型是价格、区域是各省份、自治区的、昨天到上月昨天的数据
//        List<String> quYuFrequenceList = this.getFrequenceByWhere(null, lastDayStr, null);
//        map.put("quYuJiaGeFengBu", this.convertQuYujiaGeByJiaGeZhiBiao(quYuFrequenceList, type4CommList, null, null, lastDayStr));
        return map;
    }

    public List<String> getDate(int size, String type) {
        List<String> dateList = new ArrayList<>();
        if (type.equals("yuce")) {
            for (int j = 0; j < size; j++) {
                dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), j)));
            }
        } else {
            for (int i = 0; i < size; i++) {
                dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -(size - i))));
            }
        }
        return dateList;
    }

    public List<Map<String, Object>> getList(List<Map<String, Object>> list, int size, String type) {
        List<Integer> comId = new ArrayList<>();
        for (Map<String, Object> lis : list) {
            comId.add((Integer) lis.get("index_id"));
        }
        Set set = new HashSet();

        List<Integer> newList = new ArrayList();
        for (Integer cd : comId) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        List<Map<String, Object>> reltRusult = new ArrayList<>();
        for (Integer sd : newList) {
            Map<String, Object> mp = new HashMap<>();
            List<Map<String, Object>> lt = new ArrayList();
            for (Map<String, Object> sbj : list) {
                if (sd.equals(sbj.get("index_id"))) {
                    lt.add(sbj);
                }
            }
            mp.put("list", lt);
            reltRusult.add(mp);
        }
        List<String> dateList = this.getDate(size, type);
        List<Map<String, Object>> reltRusult1 = new ArrayList<>();
        for (Map<String, Object> sbj : reltRusult) {
            Map<String, Object> mp1 = new HashMap<>();
            List<Map<String, Object>> sll1 = (List<Map<String, Object>>) sbj.get("list");
            List<String> values = new ArrayList<>();
            for (String date1 : dateList) {
                String val = "-";
                for (Map<String, Object> sbj1 : sll1) {
                    if ((sbj1.get("date").toString()).equals(date1)) {
                        val = sbj1.get("value").toString();
                    }
                }
                values.add(val);
            }
            mp1.put("xData", dateList);
            mp1.put("yData", values);
            mp1.put("commName", sll1.get(0).get("index_name"));
            mp1.put("commId", sll1.get(0).get("index_id"));
            if (type.equals("zhoushi")) {
                mp1.put("unit", sll1.get(0).get("unit"));
            }
            reltRusult1.add(mp1);
        }
        return reltRusult1;
    }

    /**
     * @Desc: 二级页面(商品总览)-根据规格品id&预测类型查询
     * @Param: 预测类型、4类商品id
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/26 11:31
     */

    public List<PssPriceReltEntity> jiaGeYuCe(String foreType, Integer commId) {

        //当天日期
        String todayStr = DateUtils.dateToStr(new Date());
        //本周最后一天
        String weekLastDayStr = DateUtils.getWeekLastDayStr();
        //本月最后一天
        String monthLastDayStr = DateUtils.getMonthLastDayStr();

        //预测类型-日、周、月
        //周预测-统计本周之后的4周数据
        if ("周预测".equals(foreType)) {
            //本周是后一天
            return pssPriceReltDao.selectByForeType(commId, foreType, weekLastDayStr, 4);
        }
        //日预测-统计30天
        if ("日预测".equals(foreType)) {
            return pssPriceReltDao.selectByForeType(commId, foreType, todayStr, 30);
        }
        //月预测-统计当前月之后的12个月数据
        if ("月预测".equals(foreType)) {
            return pssPriceReltDao.selectByForeType(commId, foreType, monthLastDayStr, 12);
        }
        return new ArrayList<>();
    }


    /**
     * @Desc: 根据时间区间、区域类型查询 频度类型
     * @Param: [startDate, endDate, areaName]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.String>
     * @Author: z.h.c
     * @Date: 2019/11/29 10:16
     */
    private List<String> getFrequenceByWhere(String startDate, String endDate, String areaName) {
        List<String> frequenceList = new ArrayList<>();
        QueryWrapper<WpBaseIndexValEntity> where5 = new QueryWrapper();
        where5.select("frequence");
        if (StringUtils.isEmpty(startDate)) {
            where5.eq("date", endDate);
        } else {
            where5.between("date", startDate, endDate);
        }
        if ("中国".equals(areaName)) {
            where5.eq("area_name", areaName);
        } else {
            where5.and(wrapper -> wrapper.likeLeft("area_name", "省").or().likeLeft("area_name", "自治区").or().likeLeft("area_name", "市"));
        }
        where5.groupBy("frequence");
        List<WpBaseIndexValEntity> entities = wpBaseIndexValDao.selectList(where5);
        entities.forEach(entity -> frequenceList.add(entity.getFrequence()));
        return frequenceList;
    }


    /**
     * @Desc: 二级页面(商品总览)-全国价格走势&区域价格分布
     * @Param: [commId, areaNmae, startDate, endDate]
     * @Return: java.util.List<io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:33
     */
    private List<WpBaseIndexValEntity> quYujiaGeByJiaGeZhiBiao(int commId, String areaName, String frequence, String startDate, String endDate) {
        QueryWrapper<WpBaseIndexValEntity> where5 = new QueryWrapper();
        where5.eq("comm_id", commId);
        if (StringUtils.isEmpty(startDate)) {
            where5.eq("date", endDate);
        } else {
            where5.between("date", startDate, endDate);
        }
        where5.eq("index_type", "价格");
        where5.eq("frequence", frequence);
        if ("中国".equals(areaName)) {
            where5.eq("area_name", areaName);
        } else {
            where5.and(wrapper -> wrapper.likeLeft("area_name", "省").or().likeLeft("area_name", "自治区").or().likeLeft("area_name", "市"));
        }
        where5.orderByAsc("date");
        return wpBaseIndexValDao.selectList(where5);
    }

    /**
     * @Desc: 根据频度、规格品在时间区域内的指标信息
     * @Param: [frequanceList, type4CommList, areaName, startDate, endDate]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/29 11:14
     */
    private Map<String, Object> convertQuYujiaGeByJiaGeZhiBiao(List<String> frequanceList,
                                                               List<PssCommTotalEntity> type4CommList,
                                                               String areaName,
                                                               String startDate,
                                                               String endDate) {
        Map<String, Object> map = new HashMap<>();
        frequanceList.forEach(frequence -> {
            Map<String, Object> commMap = new HashMap<>();
            type4CommList.forEach(commTotalEntity -> {
                List<WpBaseIndexValEntity> valEntityList = this.quYujiaGeByJiaGeZhiBiao(commTotalEntity.getCommId(), areaName, frequence, startDate, endDate);
                if (!valEntityList.isEmpty()) {
                    commMap.put(commTotalEntity.getCommId().toString(), valEntityList);
                }
            });
            if (!commMap.isEmpty()) {
                map.put(frequence, commMap);
            }
        });
        return map;
    }


    /**
     * @Desc: 根据4类商品查询所属性3类商品
     * @Param: [commId]
     * @Return: io.dfjinxin.modules.price.entity.PssCommTotalEntity
     * @Author: z.h.c
     * @Date: 2019/11/12 18:43
     */
    private PssCommTotalEntity getParantCommByCommId(Integer commId) {
        PssCommTotalEntity type3Comm = pssCommTotalDao.selectById(commId);
        if (null != type3Comm) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", type3Comm.getParentCode());
            where2.eq("level_code", 2);
            where2.eq("data_flag", 0);
            return pssCommTotalDao.selectOne(where2);
        }
        return null;
    }

    /**
     * @Desc: 首页-统计商品预警类型占比
     * @Param: [list]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:40
     */
    private Map<String, Object> contionRateVal(List<RateValDto> list) {
        Map<String, Integer> map = new HashMap<>();
        for (RateValDto entity : list) {
            if (map.containsKey(entity.getEwarnLevel())) {
                map.put(entity.getEwarnLevel(), map.get(entity.getEwarnLevel()).intValue() + 1);
            } else {
                map.put(entity.getEwarnLevel(), 1);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        //先放入默认值不存在的预警返回0
        final String rateZero = "0%";
        resultMap.put("高级预警", rateZero);
        resultMap.put("中级预警", rateZero);
//        resultMap.put("绿色预警", rateZero);
        resultMap.put("低级预警", rateZero);

        Iterator<String> iter = map.keySet().iterator();
        int num2 = list.size();
        while (iter.hasNext()) {
            String key = iter.next();
            int num1 = map.get(key);
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((float) num1 / (float) num2 * 100);
            resultMap.put(key, result + "%");
        }
        return resultMap;
    }


    /**
     * @Desc: 腾讯接口
     * @Param: []
     * @Return: int
     * @Author: z.h.c
     * @Date: 2019/11/12 10:10
     */
    public int getProgrammeDistribution() {
        logger.info("getProgrammeDistribution信息,开始--");
        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appId + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appId);
        params.put("signid", signid);
        params.put("node_userid", "0");
        final String apiUrl = "analyze/getProgrammeDistribution";
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getProgrammeDistribution req params:{}", jsonStr);
        final String url = path + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
//            logger.info("res:{}", res);
        } catch (Exception e) {
            logger.error("call-getProgrammeDistribution信息-异常:{}", e);
            return 0;
        }

        Object result = TengXunYuQing.converResult(res);
//        logger.info("the result:{}", result);
        int totalContentCnt = 0;
        if (result != null) {
            try {
                JSONObject jsonObj = (JSONObject) result;
                totalContentCnt = jsonObj.getInteger("total_content_cnt");
            } catch (Exception e) {
                logger.error("获取配置方案结果分布-解析异常{}", res);
                e.printStackTrace();
            }
        }
        return totalContentCnt;

    }

    /**
     * @Desc: 查询pss_price_ewarn表最近一月涨跌比率
     * @Param: [ewarnTypeId, asList, startDate, endDate]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2020/10/28 21:28
     */
    @Override
    public Map<String, Object> queryIndexLineData2(Integer ewarnTypeId, List<Integer> indexIds, String startDate, String endDate) {
        if (ewarnTypeId == null || indexIds.isEmpty()) return null;

        Map<String, Object> resuMap = new HashMap<>();
        for (int indexId : indexIds) {
            WpBaseIndexInfoEntity wpBaseIndexInfoEntity = wpBaseIndexInfoService.getById(indexId);
            if (wpBaseIndexInfoEntity == null) return null;
            String sourceName = ObjectUtils.isEmpty(wpBaseIndexInfoEntity.getSourceName()) ? null : wpBaseIndexInfoEntity.getSourceName();
            Integer commId = ObjectUtils.isEmpty(wpBaseIndexInfoEntity.getCommId()) ? null : wpBaseIndexInfoEntity.getCommId();
            if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
                endDate = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
                startDate = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
            }


//            List<PssPriceEwarnEntity> pssPriceEwarnEntities = this.lambdaQuery()
//                    .eq(PssPriceEwarnEntity::getCommId, commId)
//                    .eq(PssPriceEwarnEntity::getEwarnTypeId, ewarnTypeId)
//                    .eq(PssPriceEwarnEntity::getPricTypeId, indexId)
//                    .between(PssPriceEwarnEntity::getEwarnDate, startDate, endDate)
//                    .last("group by date(ewarn_date)")
//                    .list();
            Map<String, Object> mp = new HashMap<>();
            mp.put("commId", commId);
            mp.put("ewarnTypeId", ewarnTypeId);
            mp.put("pricTypeId", indexId);
            mp.put("startDate", startDate);
            mp.put("endDate", endDate);
            List<PssPriceEwarnEntity> pssPriceEwarnEntities = this.pssPriceEwarnDao.queryPriceRangeByDate(mp);
            if (!ObjectUtils.isEmpty(pssPriceEwarnEntities)) {
                StringBuilder commName = new StringBuilder(pssPriceEwarnEntities.get(0).getCommName());
                if (!StringUtils.isEmpty(sourceName)) {
                    commName.append("&").append(sourceName);
                }
                resuMap.put(commName.toString(), pssPriceEwarnEntities);
            }

//            QueryWrapper<PssPriceEwarnEntity> where = new QueryWrapper();
//            where.eq("ewarn_type_id", ewarnTypeId);
//            where.eq("Pric_type_id", indexId);
//            where.eq("comm_id", commId);
//            where.orderByAsc("ewarn_date");
//            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
//                where.between("ewarn_date", startDate, endDate);
//            } else {
//                String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
//                String last30DayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
//                where.between("ewarn_date", last30DayStr, lastDayStr);
//            }


//            //常规预警
//            if (ewarnTypeId == 18) {
//                List<PssPriceEwarnEntity> list = pssPriceEwarnDao.selectList(where);
//                if (!ObjectUtils.isEmpty(list)) {
//                    StringBuilder indexName = new StringBuilder(list.get(0).getCommName());
//                    if (!StringUtils.isEmpty(sourceName)) {
//                        indexName.append("&").append(sourceName);
//                    }
//                    resuMap.put(indexName.toString(), list);
//                }
//            }
//            //非常规预警
//            if (ewarnTypeId == 19) {
//                List<PssPriceEwarnEntity> list = pssPriceEwarnDao.selectList(where);
//                if (!ObjectUtils.isEmpty(list)) {
//                    StringBuilder indexName = new StringBuilder(list.get(0).getCommName());
//                    if (!StringUtils.isEmpty(sourceName)) {
//                        indexName.append("&").append(sourceName);
//                    }
//                    resuMap.put(indexName.toString(), list);
//                }
//            }
        }

        return resuMap;
    }


    /**
     * 根据预警类型&指标 查询指标值
     *
     * @Desc:
     * @Param: [ewarnTypeId, asList]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.util.List                                                                                                                               <                                                                                                                               java.lang.Object>>
     * @Author: z.h.c
     * @Date: 2019/11/14 14:45
     */
    @Override
    public Map<String, Object> queryIndexLineData(Integer ewarnTypeId, List<Integer> indexIds, String startDate, String endDate) {

        if (ewarnTypeId == null || indexIds.isEmpty()) return null;

        Map<String, Object> resuMap = new HashMap<>();
        for (int indexId : indexIds) {
            QueryWrapper where = new QueryWrapper();
            where.eq("index_id", indexId);
            where.orderByAsc("data_time");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                where.between("data_time", startDate, endDate);
            } else {
                String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
                String last30DayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间
                where.between("data_time", last30DayStr, lastDayStr);
            }

            WpBaseIndexInfoEntity wpBaseIndexInfoEntity = wpBaseIndexInfoService.getById(indexId);
            String sourceName = "";
            if (!ObjectUtils.isEmpty(wpBaseIndexInfoEntity)) {
                sourceName = ObjectUtils.isEmpty(wpBaseIndexInfoEntity.getSourceName()) ? null : wpBaseIndexInfoEntity.getSourceName();
            }


            //常规预警
            if (ewarnTypeId == 18) {
                List<WpCommPriEntity> list = wpCommPriDao.selectList(where);
                if (!ObjectUtils.isEmpty(list)) {
                    StringBuilder indexName = new StringBuilder(list.get(0).getIndexName());
                    if (!StringUtils.isEmpty(sourceName)) {
                        indexName.append("&").append(sourceName);
                    }
                    resuMap.put(indexName.toString(), list);
                }
            }
            //非常规预警
            if (ewarnTypeId == 19) {
                List<WpCommPriOrgEntity> list = wpCommPriOrgDao.selectList(where);
                if (!ObjectUtils.isEmpty(list)) {
                    StringBuilder indexName = new StringBuilder(list.get(0).getIndexName());
                    if (!StringUtils.isEmpty(sourceName)) {
                        indexName.append("&").append(sourceName);
                    }
                    resuMap.put(indexName.toString(), list);
                }
            }
        }

        return resuMap;
    }

    /**
     * @Desc: 根据预警类型【常规或非常规】、指标id，统考某类指标的月平均、年平均、当前值
     * @Param: [indexId, ewarnTypeId]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/14 15:41
     */
    @Override
    public Map<String, Object> queryIndexAvgByIndexId(Integer indexId, Integer ewarnTypeId) {

        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        String monthFirstDayStr = DateUtils.getMonthFirstDayStr();//本月第一天

        String lastMonthFirstDayStr = DateUtils.getLastMonthFirstDayStr();//上月第一天
        String lastMonthLastDayStr = DateUtils.getLastMonthLastDayStr();//上月最后一天

        String yearFirstDayStr = DateUtils.getYearFirstDayStr();//本年第一天

        String lastYearFirstDayStr = DateUtils.getLastYearFirstDayStr();//上年第一天
        String lastYearLastDayStr = DateUtils.getLastYearLastDayStr();//上年最后一天
        //本月、本年平均值
        Map<String, Object> currYearMonthAvg = converIndexAvg(ewarnTypeId, indexId,
                monthFirstDayStr, lastDayStr, yearFirstDayStr, lastDayStr);

        Map<String, Object> lastYearMonthAvg = converIndexAvg(ewarnTypeId, indexId,
                lastMonthFirstDayStr, lastMonthLastDayStr, lastYearFirstDayStr, lastYearLastDayStr);

        Map<String, Object> retMap = new HashMap<>();
        BigDecimal monthAvg = (BigDecimal) currYearMonthAvg.get("monthAvg");
        BigDecimal yearAvg = (BigDecimal) currYearMonthAvg.get("yearAvg");
        //昨天价格
        BigDecimal lastDayPrice = (BigDecimal) currYearMonthAvg.get("lastDayPrice");
        //前天价格
        BigDecimal last2DayPrice = (BigDecimal) currYearMonthAvg.get("last2DayPrice");
        String unit = (String) currYearMonthAvg.get("unit");

        BigDecimal zero = new BigDecimal(0);

        BigDecimal lastPriceTongBi = zero;
        if (last2DayPrice.compareTo(zero) == 1) {
            lastPriceTongBi = lastDayPrice.divide(last2DayPrice, 2, RoundingMode.HALF_UP);
        }
        //当前价格同比

        BigDecimal lastMonthAvg = (BigDecimal) lastYearMonthAvg.get("monthAvg");
        BigDecimal lastYearAvg = (BigDecimal) lastYearMonthAvg.get("yearAvg");

        BigDecimal mouthTongBi = zero;
        if (lastMonthAvg.compareTo(zero) == 1) {
            mouthTongBi = monthAvg.divide(lastMonthAvg, 2, RoundingMode.HALF_UP);
        }

        BigDecimal yearTongBi = zero;
        if (lastYearAvg.compareTo(zero) == 1) {
            yearTongBi = yearAvg.divide(lastYearAvg, 2, RoundingMode.HALF_UP);
        }

        retMap.put("currPrice", lastDayPrice.setScale(2, RoundingMode.HALF_UP));
        retMap.put("monthAvg", monthAvg);
        retMap.put("yearAvg", yearAvg);
        retMap.put("unit", unit);

        retMap.put("mouthTongBi", mouthTongBi.toString() + "%");
        retMap.put("yearTongBi", yearTongBi.toString() + "%");
        retMap.put("currPriceTongBi", lastPriceTongBi.toString() + "%");
        return retMap;
    }

    /**
     * @Desc: 大屏-首页-商品预警
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/21 14:40
     */
    @Override
    public Map<String, Object> bg_firstPage_commEwarn() {
        Map<String, Object> view = this.firstPageView(true);
        Map<String, Object> retMap = new HashMap<>();

        //商品预警信息
        retMap.put("ewarnInfo", view.getOrDefault("ewanInfo", null));
        //信息总量
        retMap.put("infoTotal", view.getOrDefault("commTotal", null));
        //橙色预警数量
        retMap.put("orangeEwarnTotal", view.getOrDefault("orangeEwarnTotal", null));
        //红色预警数量
        retMap.put("redEwarnTotal", view.getOrDefault("redEwarnTotal", null));

        QueryWrapper where = new QueryWrapper();
        where.eq("data_flag", 0);
        //商品数量
        retMap.put("commTotal", pssCommTotalDao.selectCount(where));
        return retMap;
    }

    /**
     * @Desc: 大屏-首页-风险信息
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/27 11:10
     */
    @Override
    public Map<String, Object> bg_firstPage_riskInfo() {

        Map<String, Object> retMap = new HashMap<>();
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        String last180DayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -180));//180天前时间
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("data_flag", 0);
        queryWrapper.eq("level_code", 0);
        List<PssCommTotalEntity> type1CommList = pssCommTotalDao.selectList(queryWrapper);
        //大宗&民生类 风险统计
        for (PssCommTotalEntity comm : type1CommList) {
            List<Map<String, Object>> mapList = this.ewarnCountByDate(comm.getCommId(), last180DayStr, lastDayStr);
            if (comm.getCommId() == 1) {
                //大宗
                retMap.put("daZongEwarn", mapList);
            } else {
                retMap.put("minShengEwarn", mapList);
            }
        }
        //全部预警统计
        List<Map<String, Object>> ewarnCountList = pssPriceEwarnDao.getEwarnCount(last180DayStr, lastDayStr);
        retMap.put("allEwarn", ewarnCountList);

        //风险按月走势图
        QueryWrapper<PssPriceEwarnEntity> where1 = new QueryWrapper();
        where1.eq("date(ewarn_date)", lastDayStr);
        where1.groupBy("comm_id");
        where1.orderByDesc("pri_range");
        where1.last("limit 0,3");
        List<PssPriceEwarnEntity> selectList = pssPriceEwarnDao.selectList(where1);
        List<List<List<PssPriceEwarnEntity>>> monthRiskList = new ArrayList<>();
        monthRiskList.add(getMonthRiskEwarn(selectList, last180DayStr, lastDayStr, null));//全部商品
        monthRiskList.add(getMonthRiskEwarn(selectList, last180DayStr, lastDayStr, "2"));//民生商品
        monthRiskList.add(getMonthRiskEwarn(selectList, last180DayStr, lastDayStr, "1"));//大宗商品
        retMap.put("monthRiskEwarn", monthRiskList);
        return retMap;
    }

    /**
     * @Desc: 大屏-首页-风险按月走势图-获取各一级商品TOP3走势
     * @Param: [type1CommId]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/12/3 15:00
     */
    private List<List<PssPriceEwarnEntity>> getMonthRiskEwarn(List<PssPriceEwarnEntity> selectList, String last180DayStr, String lastDayStr, String rootId) {
        List<List<PssPriceEwarnEntity>> monthRiskList = new ArrayList<>();
        for (PssPriceEwarnEntity entity : selectList) {
            List<PssPriceEwarnEntity> resList = pssPriceEwarnDao.getFirst3EwarnInfoNew(entity.getCommId(), last180DayStr, lastDayStr, rootId);
            monthRiskList.add(resList);
        }
        return monthRiskList;
    }

    /**
     * @Desc: 大屏-根据1类商品统计最近180内的各类预警级别的总数
     * @Param: [type1CommId]
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/21 15:00
     */
    private List<Map<String, Object>> ewarnCountByDate(int type1CommId, String startDateStr, String endDateStr) {
        return pssPriceEwarnDao.getEwarnCountByType1CommId(type1CommId, startDateStr, endDateStr);
    }

    private Map<String, Object> converIndexAvg(int ewarnTypeId,
                                               int indexId,
                                               String monthStartDate,
                                               String monthEndtDate,
                                               String yearStatDate,
                                               String yearEndDate) {
        Map<String, Object> map = new HashMap<>();

        List<WpCommPriDto> mouthPriceList = null;
        List<WpCommPriDto> yearPriceList = null;
        if (ewarnTypeId == 18) {
            //计算月平均
            mouthPriceList = wpCommPriDao.queryIndexByDate(indexId, monthStartDate, monthEndtDate);
            //计算年平均
            yearPriceList = wpCommPriDao.queryIndexByDate(indexId, yearStatDate, yearEndDate);
        }
        if (ewarnTypeId == 19) {
            mouthPriceList = wpCommPriOrgDao.queryIndexByDate(indexId, monthStartDate, monthEndtDate);
            yearPriceList = wpCommPriOrgDao.queryIndexByDate(indexId, yearStatDate, yearEndDate);
        }

        BigDecimal mouthTotalPrice = new BigDecimal(0);

        for (WpCommPriDto entity : mouthPriceList) {
            mouthTotalPrice = mouthTotalPrice.add(entity.getValue());
        }

        BigDecimal lastDayPrice = new BigDecimal(0);
        BigDecimal last2DayPrice = new BigDecimal(0);
        String unit = "";
        //是否存在昨天(最新)价格
        if (mouthPriceList != null && mouthPriceList.size() > 0) {
            lastDayPrice = mouthPriceList.get(0).getValue();
            unit = mouthPriceList.get(0).getUnit();
        }

        //是否存在前天价格
        if (mouthPriceList != null && mouthPriceList.size() > 1) {
            last2DayPrice = mouthPriceList.get(1).getValue();
        }

        map.put("lastDayPrice", lastDayPrice);
        map.put("last2DayPrice", last2DayPrice);
        map.put("unit", unit);

//        long diffDay = DateUtils.getMonthDiffDay();
        BigDecimal mouthAvg = mouthTotalPrice.divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
        map.put("monthAvg", mouthAvg);

        BigDecimal yearTotalPrice = new BigDecimal(0);
//        long yearDiffDay = DateUtils.getYearDiffDay();
        for (WpCommPriDto entity : yearPriceList) {
            yearTotalPrice = yearTotalPrice.add(entity.getValue());
        }
        BigDecimal yearAvg = yearTotalPrice.divide(new BigDecimal(365), 2, RoundingMode.HALF_UP);
        map.put("yearAvg", yearAvg);
        return map;
    }

    /**
     * @Desc:二级页面-价格预警指标查询
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2020/12/07 11:10
     */
    @Override
    public List<Map<String, Object>> warningDistribution(Map<String, Object> params) {
        List<Map<String, Object>> indexList = baseMapper.getThisYearErawCommList(params);
        if (indexList.size() > 0) {
            for (Map<String, Object> lis : indexList) {
                params.put("commId", lis.get("comm_id"));
                lis.put("subList", baseMapper.getThisYearErawIndexList(params));
            }
        }
        return indexList;
    }

    /**
     * @Desc:二级页面-价格预警指标数据查询
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2020/12/07 11:10
     */
    @Override
    public List<Map<String, Object>> warningIndexDate(Map<String, Object> params) {
        List<Map<String, Object>> lis = new ArrayList<>();
        List<String> dateList = this.getDate(Calendar.getInstance().get(Calendar.DAY_OF_YEAR), "yujing");
        List<String> ids = (List<String>) params.get("indexId");
        String str1 = "0";
        String str2 = "0";
        if (ids.size() > 0) {
            for (String id : ids) {
                params.put("indexId", id);
                List<Map<String, Object>> ls = baseMapper.warningIndexDate(params);
                Map<String, Object> map = new HashMap<>();
                List<String> huanbiData = new ArrayList<>();
                List<String> tongbiData = new ArrayList<>();
                String indexName = "";
                String unit = "";
                if (ls.size() > 0) {
                    for (String itrm1 : dateList) {
                        for (Map<String, Object> itrm : ls) {
                            if (itrm1.equals(itrm.get("date").toString())) {
                                str1 = itrm.get("pri_range").toString();
                                str2 = itrm.get("pri_yonyear").toString();
                                indexName = itrm.get("index_name").toString();
                                unit = itrm.get("unit").toString();
                            }
                        }
                        huanbiData.add(str1);
                        tongbiData.add(str2);
                    }
                }
                map.put("indexName", indexName);
                map.put("unit", unit);
                map.put("xData", dateList);
                map.put("huanbiData", huanbiData);
                map.put("tongbiData", tongbiData);
                lis.add(map);
            }
        }
        return lis;
    }

    /**
     * @Desc:二级页面-价格预警预警区间值查询
     * @Param: []
     * @Return: java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @Author: y.b
     * @Date: 2020/12/07 11:10
     */
    @Override
    public Map<String, Object> getEwarValue(Map<String, Object> params) {
        List<Map<String, Object>> list = baseMapper.getEwarValue(params);
        Map<String, Object> map = new HashMap<>();
        if (list.size() > 0) {
            List<String> ls1 = new ArrayList<>();
            ls1.add(list.get(0).get("ewarn_llmt_yellow").toString());
            ls1.add(list.get(0).get("ewarn_ulmt_yellow").toString());
            map.put("diji", ls1);
            List<String> ls2 = new ArrayList<>();
            ls2.add(list.get(0).get("ewarn_llmt_orange").toString());
            ls2.add(list.get(0).get("ewarn_ulmt_orange").toString());
            map.put("zhongji", ls2);
            List<String> ls3 = new ArrayList<>();
            ls3.add(list.get(0).get("ewarn_llmt_red").toString());
            ls3.add(list.get(0).get("ewarn_ulmt_red").toString());
            map.put("gaoji", ls3);
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> viewBy(Map<String, Object> params) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
        if ("1".equals(params.get("type").toString())) {
            if ("周".equals(params.get("dateType").toString())) {
                params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -7)));
            } else if ("月".equals(params.get("dateType").toString())) {
                params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -30)));
            } else {
                params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -365)));
            }
        } else {
            params.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), 0)));
            if ("周".equals(params.get("dateType").toString())) {
                params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), +6)));
            } else if ("月".equals(params.get("dateType").toString())) {
                params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), +29)));
            }
        }
        List<String> ids = (List<String>) params.get("indexId");
        if (ids.size() > 0) {
            if (ids.size() == 1) {
                List<Map<String, Object>> list1 = new ArrayList<>();
                params.put("indexId", ids.get(0));
                if ("1".equals(params.get("type").toString())) {
                    list1 = wpBaseIndexValDao.getIndexThend(params);
                } else {
                    list1 = baseMapper.getForePriceThend(params);
                }
                if (list1.size() > 0) {
                    Map<String, Object> map1 = new HashMap<>();
                    List<String> yData = new ArrayList<>();
                    List<String> xData = new ArrayList<>();
                    String indexName = list1.get(0).get("index_name").toString();
                    String indexId = list1.get(0).get("index_id").toString();
                    String unit = list1.get(0).get("unit").toString();
                    for (Map<String, Object> l1 : list1) {
                        yData.add(l1.get("value").toString());
                        xData.add(l1.get("date").toString());
                    }
                    map1.put("indexName", indexName);
                    map1.put("unit", unit);
                    map1.put("indexId", indexId);
                    map1.put("xData", xData);
                    map1.put("yData", yData);
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
                        list2 = wpBaseIndexValDao.getIndexThend(params);
                    } else {
                        list2 = baseMapper.getForePriceThend(params);
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
                        dataList.add(map2);
                    }
                }
            }
        }
        return dataList;
    }

    @Override
    public Map<String, Object> fore(Map<String, Object> params) {
        params.put("satrtDate1", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), 0)));
        Map<String, Object> map = new HashMap<>();
        params.put("foreType", "日预测");
        List<Map<String, Object>> indexList = baseMapper.getForeIndex(params);
        params.put("foreType", "年预测");
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -0)); //设置时间为当前时间
        ca.add(Calendar.YEAR, 0); //年份减1
        params.put("satrtDate1", new SimpleDateFormat("yyyy").format(ca.getTime()));
        List<Map<String, Object>> indexList2 = baseMapper.getForeIndex(params);
        map.put("duanqiIndexList", indexList);
        map.put("chanqiIndexList", indexList2);
        return map;
    }

    @Override
    public List<Map<String, Object>> foreData(Map<String, Object> params) {
        List<Map<String, Object>> duanqiEwar1 = new ArrayList<>();
        if ("1".equals(params.get("type").toString())) {
            params.put("satrtDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -30)));
            params.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)));
            params.put("satrtDate1", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), 0)));
           duanqiEwar1 = wpBaseIndexValDao.getDayFore(params);
            String unit = "";
            if(duanqiEwar1.size()>0){
                unit = duanqiEwar1.get(0).get("unit").toString();
            }
            List<Map<String, Object>> duanqiEwar2 = baseMapper.getDayFore(params);
            if(duanqiEwar2.size()>0){
                for (Map<String, Object> l3 : duanqiEwar2) {
                    l3.put("unit",unit);
                    duanqiEwar1.add(l3);
                }
            }
        }else{
            Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
            ca.setTime(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -0)); //设置时间为当前时间
            ca.add(Calendar.YEAR, 0); //年份减1
            params.put("satrtYear", new SimpleDateFormat("yyyy").format(ca.getTime()));
            duanqiEwar1 = baseMapper.getYearFore(params);
        }
        return duanqiEwar1;
    }

    @Override
    public Map<String, Object> ewarmMap(Map<String, Object> params) {
        String commId = params.get("commId").toString();
        //获取最后一记录的日期
        String format = baseMapper.getLastRecordDate(commId);
        Map<String, Object> mapp = new HashMap<>();
        Map<String, Object> maxErawInfo = baseMapper.getMaxPro(format,commId);
        mapp.put("maxErawInfo",maxErawInfo);

        //全国的省份以及市
        Map<String, List<ChinaAreaInfo>> listMap = handleChinaAreaInfo();
        //获取数据库中存在的 省份数据
        List<AreaPrice> areaPrice2 = baseMapper.getAreaPrice2(commId,null);
        //当省份没有获取到数据的时候 , 获取改省份下的城市 作为省份的价格
        if (areaPrice2.size() < listMap.size()){
            //有价格的省份
            List<String> collect = areaPrice2.stream().map(AreaPrice::getAreaName).collect(Collectors.toList());
            listMap.forEach((k, v) -> {
                if (!collect.contains(k)) {
                    //获取没有该省份价格下的城市
                    Set<String> cityList = v.get(0).getChinaAreaInfos().stream().map(ChinaAreaInfo::getAreaName)
                            .collect(Collectors.toSet());
                    if (cityList.isEmpty()){
                        areaPrice2.add(new AreaPrice(commId, k, null));
                    } else {
                        List<AreaPrice> areaPrice = baseMapper.getAreaPrice2(commId, cityList);
                        if (areaPrice.isEmpty()) {
                            //当该省份的城市价格为空,修改省份为空
                            areaPrice2.add(new AreaPrice(commId, k, null));
                        } else {
                            //否则设置该市的价格为该省份的价格
                            areaPrice2.add(new AreaPrice(commId, k, areaPrice.get(0).getPrice()));
                        }
                    }
                }
            });
        }
        mapp.put("mapData",areaPrice2);
        return mapp;
    }

    /***
     * @Author LiangJianCan
     * @Description  获取中国的省份以及省份里面的城市
     * @Date 2021/4/21 15:35
     * @Param []
     * @return java.util.Map<java.lang.String,java.util.List<io.dfjinxin.modules.price.dto.ChinaAreaInfo>>
     **/
    private Map<String, List<ChinaAreaInfo>> handleChinaAreaInfo(){
        List<ChinaAreaInfo> chinaAreaInfo = baseMapper.getChinaAreaInfo();

        Map<String, List<ChinaAreaInfo>> listMap = chinaAreaInfo.stream().filter((entity) ->
                entity.getParentId() == 0).peek((entity -> {
            //获取当前菜单的子菜单
            entity.setChinaAreaInfos(getChildren(entity, chinaAreaInfo));
        })).collect(Collectors.groupingBy(ChinaAreaInfo::getAreaName));

        return listMap;
    }

    private List<ChinaAreaInfo> getChildren(ChinaAreaInfo root, List<ChinaAreaInfo> all) {
        List<ChinaAreaInfo> list = all.stream().filter(entity -> {
            return entity.getParentId().equals(root.getAreaId());
        }).collect(Collectors.toList());
        return list;
    }
}