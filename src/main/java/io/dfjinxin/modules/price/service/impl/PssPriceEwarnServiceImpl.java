package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.MD5Utils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.common.utils.echart.HttpUtil;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.dao.*;
import io.dfjinxin.modules.price.dto.PwwPriceEwarnDto;
import io.dfjinxin.modules.price.dto.RateValDto;
import io.dfjinxin.modules.price.entity.*;
import io.dfjinxin.modules.price.service.PssCommConfService;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.dfjinxin.modules.yuqing.TengXunYuQing;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private HiveService hiveService;

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

        Map<String, Object> map = firstPageView();
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
     * @Desc: 首页展示 第二次修改
     * @Param: []
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/10/23 13:56
     */
    @Override
    public Map<String, Object> firstPageView() {
        Map<String, Object> retMap = new HashMap<>();

        List<PssPriceEwarnEntity> yestDayMaxPricEwarnList = new ArrayList<>();
        QueryWrapper where1 = new QueryWrapper();
        where1.groupBy("comm_id");
        List<PssPriceEwarnEntity> priceEwarnList = pssPriceEwarnDao.selectList(where1);
        //根据分组id,查询当前日期前一天的数据
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
        for (PssPriceEwarnEntity entity : priceEwarnList) {
            List<PssPriceEwarnEntity> entities = pssPriceEwarnDao.queryPriceEwarnByDate(entity.getCommId(), lastDayStr);
            if (entities == null || entities.size() < 1) {
                continue;
            }
            yestDayMaxPricEwarnList.add(entities.get(0));
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
                entity.setEwarnLevel(asciiInfoEntity.getCodeName());
            }
            ewanInfoList.add(entity);
            //用于计算预警类型占比
            RateValDto rateValDto = new RateValDto();
            rateValDto.setEwanName(asciiInfoEntity.getCodeName());
            rateValDto.setEwarnLevel(entity.getEwarnLevel());
            rateValDtos.add(rateValDto);
        }

        int hiveCount = getHiveCount();
        int tengxunCount = getProgrammeDistribution();

        //step1,实时预览-总量(万）
        retMap.put("commTotal", hiveCount + tengxunCount);

        Map<String, Object> lineDateMap = new HashMap<>();
        QueryWrapper<PssPriceEwarnEntity> queryWrapper = new QueryWrapper();
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
                ewarnLevelMap.put("红色预警", dtoList);
            }
            if ("72".equals(entity.getEwarnLevel())) {
                ewarnLevelMap.put("橙色预警", dtoList);
            }
            if ("73".equals(entity.getEwarnLevel())) {
                ewarnLevelMap.put("黄色预警", dtoList);
            }
            if ("74".equals(entity.getEwarnLevel())) {
                ewarnLevelMap.put("绿色预警", dtoList);
            }
            lineDateMap.putAll(ewarnLevelMap);
        }
        //step2,商品预警趋势-按预警级别分组统计 从本月一号到昨天预警数据
        retMap.put("priVal", lineDateMap);

        //step3,商品预警分布-昨天商品预警类型占比
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
                if (level == 74) {
                    countMap.put("greenEwarnTotal", total);
                }
            }
        }

        //step5,昨日各种预警级别的数量
        Map<String, Object> temp = new HashMap<>();
        temp.put("redEwarnTotal", 0);
        temp.put("orangeEwarnTotal", 0);
        temp.put("yellowEwarnTotal", 0);
        temp.put("greenEwarnTotal", 0);
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

        Collections.sort(set, new Comparator<PwwPriceEwarnDto>() {
            @Override
            public int compare(PwwPriceEwarnDto u1, PwwPriceEwarnDto u2) {
                if (u1.getEwarnDate().compareTo(u2.getEwarnDate()) == 0) {
                    return -1;
                }
                return u1.getEwarnDate().compareTo(u2.getEwarnDate());
            }
        });
    }

    @Override
    public List<PssPriceEwarnEntity> getDayReportData(Map<String, Object> params) {

        QueryWrapper<PssPriceEwarnEntity> qw = new QueryWrapper<PssPriceEwarnEntity>();
        Date startDate = (Date) params.get("startDate");
        Date endDate = (Date) params.get("endDate");
        String commId = params.get("commId") + "";
        qw.eq("comm_id", commId);
        qw.between("ewarn_date", startDate, endDate);
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
    public Map<String, Object> secondPageDetail(Integer commId) {

        Map<String, Object> map = new HashMap<>();

        // step 1、计算环比、同比 无数据此内容返回null
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        String lastMonthDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));//一个月前时间

        // 查询昨天涨幅最大的4类商品预警
        PssPriceEwarnEntity entity = pssPriceEwarnDao.selectMaxRange(commId, lastDayStr);
        if (entity != null) {
            // 昨天价格
            BigDecimal lastPriValue = entity.getPriValue();
            BigDecimal ONE = new BigDecimal(1);
            BigDecimal HUN = new BigDecimal(100);

            Date last2Date = DateUtils.addDateDays(entity.getEwarnDate(), -1);
            String last2DateStr = DateUtils.dateToStr(last2Date);//前天时间
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("Date(ewarn_date)", last2DateStr);
            where2.eq("comm_id", entity.getCommId());
            where2.last(" limit 0,1");
            PssPriceEwarnEntity entity1 = pssPriceEwarnDao.selectOne(where2);
            if (entity1 == null) {
                map.put("tongBi", 0);
                logger.error("商品{}-{},预警价格,数据不存在!", entity.getCommId(), last2DateStr);
            } else {
                // 前天价格
                BigDecimal last2DayPriValue = entity1.getPriValue();
                // 同比
                BigDecimal tongBiTemp = lastPriValue.divide(last2DayPriValue, 2, RoundingMode.HALF_UP).subtract(ONE);
                String tongBi = tongBiTemp.multiply(HUN).toString() + "%";
                map.put("tongBi", tongBi);
            }

            //计算上月今日价格-计算环比
            QueryWrapper where4 = new QueryWrapper();
            where4.eq("Date(ewarn_date)", lastMonthDayStr);
            where4.eq("comm_id", entity.getCommId());
            where4.last("limit 0,1");

            PssPriceEwarnEntity entity2 = pssPriceEwarnDao.selectOne(where4);

            if (entity2 == null) {
                map.put("huanBi", 0);
                logger.error("商品{}-{},预警价格,数据不存在!", entity.getCommId(), lastMonthDayStr);
            } else {
                //上月今日价格
                BigDecimal lastMonthTodayPrice = entity2.getPriValue();
                // 环比
                BigDecimal huanBiTemp = lastPriValue.divide(lastMonthTodayPrice, 2, RoundingMode.HALF_UP).subtract(ONE);
                String huanBi = huanBiTemp.multiply(HUN).toString() + "%";
                map.put("huanBi", huanBi);
            }
        } else {
            map.put("huanBi", 0);
            map.put("tongBi", 0);
        }

        //step2,规格品价格数据-统计昨天各规格品的商品价格数据
        final String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        List<WpBaseIndexValEntity> lastDayValList = wpBaseIndexValDao.queryLastDayPriceByCommId(commId, lastDayStr);
        map.put("priceList", lastDayValList);

        //step3,地图数据-统计规格品指标类型为'价格'的各省份昨天价格数据
        List<WpBaseIndexValEntity> mapData = wpBaseIndexValService.getprovinceLastDayMapData(commId, "价格", lastDayStr);
        map.put("provinceMap", mapData);

        //step3,生产数据情况
        List<WpBaseIndexValEntity> prodData = wpBaseIndexValService.getprovinceLastDayMapData(commId, "生产", lastDayStr);
        map.put("prodData", prodData);

        //step4,全国价格走势 规格品指标类型是价格、区域是全国的、上月昨天到昨天的数据
        List<WpBaseIndexValEntity> quanGuoJiaGeZouShi = this.quYujiaGeByJiaGeZhiPiao(commId, "全国", lastMonthDayStr, lastDayStr);
        map.put("quanGuoJiaGeZouShi", quanGuoJiaGeZouShi);

        //step5,区域价格分布 规格品指标类型是价格、区域是各省份、自治区的、昨天到上月昨天的数据
        List<WpBaseIndexValEntity> quYuJiaGeFengBu = this.quYujiaGeByJiaGeZhiPiao(commId, "", lastMonthDayStr, lastDayStr);
        map.put("quYuJiaGeFengBu", quYuJiaGeFengBu);

        //step6,价格预测情况 统计规格品各种预测类型
        QueryWrapper<PssPriceReltEntity> where5 = new QueryWrapper();
        where5.inSql("comm_id", sql);
        where5.groupBy("comm_id");
        List<PssPriceReltEntity> reltEntityList = pssPriceReltDao.selectList(where5);

        Map<String, Object> reltRusult = new HashMap<>();
        for (PssPriceReltEntity reltEntity : reltEntityList) {
            QueryWrapper<PssPriceReltEntity> where6 = new QueryWrapper();
            where6.eq("comm_id", reltEntity.getCommId());
            where6.groupBy("fore_type");
            List<PssPriceReltEntity> reltTypeList = pssPriceReltDao.selectList(where6);
            reltRusult.put(reltEntity.getCommId().toString(), jiaGeYuCe(reltTypeList));
        }
        map.put("jiaGeYuCe", reltRusult);
        return map;
    }

    /**
     * @Desc: 二级页面(商品总览)-价格预测情况
     * @Param: [reltTypeList]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:31
     */
    private Map<String, Object> jiaGeYuCe(List<PssPriceReltEntity> reltTypeList) {

        //当天日期
        String todayStr = DateUtils.dateToStr(new Date());
        //本周最后一天
        String weekLastDayStr = DateUtils.getWeekLastDayStr();
        //本月最后一天
        String monthLastDayStr = DateUtils.getMonthLastDayStr();
        Map<String, Object> map = new HashMap<>();
        for (PssPriceReltEntity entity : reltTypeList) {
            QueryWrapper<PssPriceReltEntity> where5 = new QueryWrapper();
            where5.eq("comm_id", entity.getCommId());
            where5.orderByAsc("fore_time");

            //预测类型-日、周、月
            //周预测-统计本周之后的4周数据
            if ("周预测".equals(entity.getForeType())) {
                //本周是后一天
                where5.eq("fore_type", "周预测");
                where5.gt("fore_time", weekLastDayStr);
                where5.last(" limit 0,4");
                map.put("周预测", pssPriceReltDao.selectList(where5));
            }
            //日预测-统计30天
            if ("日预测".equals(entity.getForeType())) {
                where5.eq("fore_type", "日预测");
                where5.gt("fore_time", todayStr);
                where5.last(" limit 0,30");
                map.put("日预测", pssPriceReltDao.selectList(where5));

            }
            //月预测-统计当前月之后的12个月数据
            if ("月预测".equals(entity.getForeType())) {
                where5.eq("fore_type", "月预测");
                where5.gt("fore_time", monthLastDayStr);
                where5.last(" limit 0,12");
                map.put("月预测", pssPriceReltDao.selectList(where5));
            }
        }
        return map;
    }

    /**
     * @Desc: 二级页面(商品总览)-全国价格走势&区域价格分布
     * @Param: [commId, areaNmae, startDate, endDate]
     * @Return: java.util.List<io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:33
     */
    private List<WpBaseIndexValEntity> quYujiaGeByJiaGeZhiPiao(int commId, String areaNmae, String startDate, String endDate) {
        final String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        QueryWrapper<WpBaseIndexValEntity> where5 = new QueryWrapper();
        where5.inSql("comm_id", sql);
        where5.between("date", startDate, endDate);
        where5.eq("index_type", "价格");
        if (areaNmae.equals("全国")) {
            where5.eq("area_name", areaNmae);
        } else {
            where5.and(wrapper -> wrapper.likeLeft("area_name", "省").or().likeLeft("area_name", "自治区"));
        }
        where5.groupBy("comm_id");
        return wpBaseIndexValDao.selectList(where5);
    }

    /**
     * @Desc: 根据4类商品查询所属性3类商品
     * @Param: [commId]
     * @Return: io.dfjinxin.modules.price.entity.PssCommTotalEntity
     * @Author: z.h.c
     * @Date: 2019/11/12 18:43
     */
    private PssCommTotalEntity getParantCommByCommId(Integer commId) {
        PssCommTotalEntity level_code3Comm = pssCommTotalDao.selectById(commId);
        if (null != level_code3Comm) {
            QueryWrapper where2 = new QueryWrapper();
            where2.eq("comm_id", level_code3Comm.getParentCode());
            where2.eq("level_code", 2);
            where2.eq("data_flag", 0);
            PssCommTotalEntity entity = pssCommTotalDao.selectOne(where2);
            return entity;
        }
        return null;
    }

    /**
     * @Desc: 首页-统计商品预警类型占比
     * @Param: [list]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:40
     */
    private Map<String, Object> contionRateVal(List<RateValDto> list) {
        Map<String, Integer> map = new HashMap<>();
        for (RateValDto entity : list) {
            if (map.containsKey(entity.getEwarnLevel())) {
                map.put(entity.getEwarnLevel(), map.get(entity.getEwarnLevel()).intValue() + 1);
            } else {
                map.put(entity.getEwarnLevel(), new Integer(1));
            }
        }
        Map<String, Object> resultMap = new HashMap<>();

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
     * @Desc: 首页-统计商品总数量
     * @Param: [list]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/12 18:40
     */
    private int getHiveCount() {
        final String sql_1 = "select count(*) tol from wp_base_index_val t";
        final String sql_2 = "select count(*) tol from wp_marco_index_val t";

        List<String> sqlList = new ArrayList<>();
        sqlList.add(sql_1);
        sqlList.add(sql_2);

        Long totalCount = 0L;
        List<Map<String, Object>> data = new ArrayList<>();
        for (String sql : sqlList) {
            List<Map<String, Object>> res = hiveService.selectData(sql);
            if (res != null && res.size() > 0) {
                data.add(res.get(0));
            }
        }
        if (data == null || data.size() == 0) {
            return 0;
        }
        for (Map<String, Object> map : data) {
            if (map.containsKey("tol")) {
                totalCount += (Long) map.get("tol");
            }
        }
        return totalCount.intValue();
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
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(TengXunYuQing.APPID + TengXunYuQing.PWD));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", TengXunYuQing.APPID);
        params.put("signid", signid);
        params.put("node_userid", "0");
        final String apiUrl = "analyze/getProgrammeDistribution";
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getProgrammeDistribution req params:{}", jsonStr);
        final String url = TengXunYuQing.PATH + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
            logger.info("res:{}", res);
        } catch (Exception e) {
            logger.error("call-getProgrammeDistribution信息-异常:{}", e);
            return 0;
        }

        Object result = TengXunYuQing.converResult(res);
        logger.info("the result:{}", result);
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
     * 根据预警类型&指标 查询指标值
     *
     * @Desc:
     * @Param: [ewarnTypeId, asList]
     * @Return: java.util.Map<java.lang.String, java.util.List < java.lang.Object>>
     * @Author: z.h.c
     * @Date: 2019/11/14 14:45
     */
    @Override
    public Map<String, Object> queryIndexLineData(Integer ewarnTypeId, List<Integer> indexIds, String startDate, String endDate) {

        if (ewarnTypeId == null || indexIds == null || indexIds.size() < 1) return null;

        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));//昨天时间
        Map<String, Object> resuMap = new HashMap<>();
        for (int indexId : indexIds) {
            QueryWrapper where = new QueryWrapper();
            where.eq("index_id", indexId);
            where.le("data_time", lastDayStr);
            where.orderByDesc("data_time");
            if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
                where.between("data_time", startDate, endDate);
            } else {
                where.last("limit 0,30");
            }

            //常规预警
            if (ewarnTypeId == 18) {
                List<WpCommPriEntity> list = wpCommPriDao.selectList(where);
                if (list != null && list.size() > 1) {
                    resuMap.put(list.get(0).getIndexName(), list);
                }
            }
            //非常规预警
            if (ewarnTypeId == 19) {
                List<WpCommPriEntity> list = wpCommPriOrgDao.selectList(where);
                if (list != null && list.size() > 1) {
                    resuMap.put(list.get(0).getIndexName(), list);
                }
            }
        }

        return resuMap;

    }

    /**
     * @Desc: 根据预警类型【常规或非常规】、指标id，统考某类指标的月平均、年平均、当前值
     * @Param: [indexId, ewarnTypeId]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
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

        String LastYearFirstDayStr = DateUtils.getLastYearFirstDayStr();//上年第一天
        String LastYearLastDayStr = DateUtils.getLastYearLastDayStr();//上年最后一天
        //本月、本年平均值
        Map<String, Object> currYearMonthAvg = converIndexAvg(ewarnTypeId, indexId,
                monthFirstDayStr, lastDayStr, yearFirstDayStr, lastDayStr);

        Map<String, Object> lastYearMonthAvg = converIndexAvg(ewarnTypeId, indexId,
                lastMonthFirstDayStr, lastMonthLastDayStr, LastYearFirstDayStr, LastYearLastDayStr);

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

        retMap.put("currPrice", lastDayPrice);
        retMap.put("monthAvg", monthAvg);
        retMap.put("yearAvg", yearAvg);
        retMap.put("unit", unit);

        retMap.put("mouthTongBi", mouthTongBi.toString() + "%");
        retMap.put("yearTongBi", yearTongBi.toString() + "%");
        retMap.put("currPriceTongBi", lastPriceTongBi.toString() + "%");
        return retMap;
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

}
