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
import io.dfjinxin.modules.price.dto.RateValDto;
import io.dfjinxin.modules.price.entity.*;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.dfjinxin.modules.yuqing.TengXunYuQing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
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
    WpAsciiInfoDao wpAsciiInfoDao;

    @Autowired
    WpBaseIndexValDao wpBaseIndexValDao;

    @Autowired
    PssPriceReltDao pssPriceReltDao;

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
        resMap.put("ewanInfo",map.get("ewanInfo"));
        return resMap;
        /*Map<String, Object> result = new HashMap<>();
        List<PssPriceEwarnEntity> priceEwarnEntityList = (List<PssPriceEwarnEntity>) map.get("ewanInfo");
        List<PssPriceEwarnEntity> dazong = new ArrayList();
        List<PssPriceEwarnEntity> minsheng = new ArrayList();
        for (PssPriceEwarnEntity entity : priceEwarnEntityList) {
            //取到的是3级商品的id，计算该3类商品属于哪一类商品
            PssCommTotalEntity tyep1Comm = pssCommTotalDao.getType1CommBySubCommId(entity.getCommId());
            //大宗商品
            if (tyep1Comm != null && tyep1Comm.getCommId() == 1) {
                dazong.add(entity);
            } else {
                minsheng.add(entity);
            }
        }
        result.put("dazong", dazong);
        result.put("minsheng", minsheng);*/
//        return result;
    }

    @Override
    public List<Object> queryDetail(Integer commId, Integer ewarnTypeId) {

        String startDateStr = DateUtils.getMonthFirstDayStr();
        String endDateStr = DateUtils.getCurrentDayStr();
        List<PssPriceEwarnEntity> pssPriceEwarnEntities = pssPriceEwarnDao.queryEchartsData(commId, ewarnTypeId, startDateStr, endDateStr);
        List<BigDecimal> echartsData = new ArrayList<>();
        for (PssPriceEwarnEntity entity : pssPriceEwarnEntities) {
            echartsData.add(entity.getPriValue());
        }

        Map<String, Object> map = new HashMap();
        map.put("echartsData", echartsData);


        //计算月平均
        QueryWrapper where2 = new QueryWrapper();
        where2.in("comm_id", commId);
        where2.between("data_time", DateUtils.getMonthFirstDayStr(), DateUtils.getCurrentDayStr());
        where2.orderByDesc("data_time");
        List<WpCommPriEntity> mouthPriceList = wpCommPriDao.selectList(where2);
        BigDecimal currentPrice = new BigDecimal(0);
        if (mouthPriceList != null && mouthPriceList.size() > 0) {
            currentPrice = mouthPriceList.get(0).getPriToday();
        }
        BigDecimal mouthTotalPrice = new BigDecimal(0);
        for (WpCommPriEntity entity : mouthPriceList) {
            mouthTotalPrice = mouthTotalPrice.add(entity.getPriToday());
        }
        long diffDay = DateUtils.getMonthDiffDay();
        BigDecimal mouthAvg = mouthTotalPrice.divide(new BigDecimal(diffDay), 2, RoundingMode.HALF_UP);

        //计算年平均
        QueryWrapper where3 = new QueryWrapper();
        where3.in("comm_id", commId);
        where3.between("data_time", DateUtils.getYearFirstDayStr(), DateUtils.getCurrentDayStr());
        List<WpCommPriEntity> yearPriceList = wpCommPriDao.selectList(where3);
        BigDecimal yearTotalPrice = new BigDecimal(0);
        long yearDiffDay = DateUtils.getYearDiffDay();
        for (WpCommPriEntity entity : yearPriceList) {
            yearTotalPrice = yearTotalPrice.add(entity.getPriToday());
        }
        BigDecimal yearAvg = yearTotalPrice.divide(new BigDecimal(yearDiffDay), 2, RoundingMode.HALF_UP);
        map.put("monthAvg", mouthAvg);
        map.put("yearAvg", yearAvg);
        map.put("currPrice", currentPrice);
        List<Object> resultList = new ArrayList<>();
        resultList.add(map);
        return resultList;
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
            entity.setCommName(commTotalEntity.getCommName());
            entity.setCommId(commTotalEntity.getCommId());
            entity.setEwarnLevel(asciiInfoEntity.getCodeName());
            ewanInfoList.add(entity);
            //用于计算预警类型占比
            RateValDto rateValDto = new RateValDto();
            rateValDto.setEwanName(asciiInfoEntity.getCodeName());
            rateValDto.setEwarnLevel(entity.getEwarnLevel());
            rateValDtos.add(rateValDto);
        }

        int hiveCount = getHiveCount();
        int tengxunCount = getProgrammeDistribution();
        retMap.put("commTotal", hiveCount + tengxunCount);
        //最近所有商品一个月涨跌值
        QueryWrapper where3 = new QueryWrapper();
        //一个月前
        where3.between("Date(ewarn_date)", DateUtils.getLastMonthByVal(1), lastDayStr);
//        一年前
//        where3.between("Date(ewarn_date)", DateUtils.getLastYearByVal(1), DateUtils.getCurrentDayStr());
        List<PssPriceEwarnEntity> monthPriRangeList = pssPriceEwarnDao.selectList(where3);
        retMap.put("priVal", monthPriRangeList);

        //当前天商品预警类型占比
        retMap.put("rateVel", contionRateVal(rateValDtos));
        //商品预警详情
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

        Map<String, Object> temp = new HashMap<>();
        temp.put("redEwarnTotal", 0);
        temp.put("orangeEwarnTotal", 0);
        temp.put("yellowEwarnTotal", 0);
        temp.put("greenEwarnTotal", 0);
        temp.putAll(countMap);
        retMap.putAll(temp);
        return retMap;
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
     * 计算二级页面除指标类型信息外的其它数据
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

        //step2,统计昨天各规格品的商品价格
        final String sql = "select pss_comm_total.comm_id from pss_comm_total where data_flag=0 and parent_code=" + commId;
        QueryWrapper where3 = new QueryWrapper();
        where3.inSql("comm_id", sql);
        where3.eq("date", lastDayStr);
        where3.groupBy("comm_id");
        List<WpBaseIndexValEntity> lastDayValList = wpBaseIndexValDao.selectList(where3);
        map.put("priceList", lastDayValList);

        //step3,规格品价格数据
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
    * @Desc:  二级页面-价格预测情况
    * @Param: [reltTypeList]
    * @Return: java.util.Map<java.lang.String,java.lang.Object>
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
    * @Desc:  二级页面-全国价格走势&区域价格分布
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
    * @Desc:  根据4类商品查询所属性3类商品
    * @Param: [commId]
    * @Return: io.dfjinxin.modules.price.entity.PssCommTotalEntity
    * @Author: z.h.c
    * @Date: 2019/11/12 18:43
    */
    private PssCommTotalEntity getParantCommByCommId(Integer commId) {
        PssCommTotalEntity level_code3Comm = pssCommTotalDao.selectById(commId);
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("comm_id", level_code3Comm.getParentCode());
        where2.eq("level_code", 2);
        PssCommTotalEntity entity = pssCommTotalDao.selectOne(where2);
        return entity;
    }

    /**
    * @Desc:  首页-统计商品预警类型占比
    * @Param: [list]
    * @Return: java.util.Map<java.lang.String,java.lang.Object>
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
     * @Desc:  首页-统计商品总数量
     * @Param: [list]
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
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
            JSONObject jsonObj = (JSONObject) result;
            totalContentCnt = jsonObj.getInteger("total_content_cnt");
        }
        return totalContentCnt;

    }

}
