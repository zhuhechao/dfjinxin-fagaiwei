package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.dao.PssPriceEwarnDao;
import io.dfjinxin.modules.price.dao.WpAsciiInfoDao;
import io.dfjinxin.modules.price.dao.WpCommPriDao;
import io.dfjinxin.modules.price.dto.RateValDto;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;
import io.dfjinxin.modules.price.entity.WpCommPriEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;


@Service("pssPriceEwarnService")
public class PssPriceEwarnServiceImpl extends ServiceImpl<PssPriceEwarnDao, PssPriceEwarnEntity> implements PssPriceEwarnService {

    @Autowired
    PssPriceEwarnDao pssPriceEwarnDao;
    @Autowired
    PssCommTotalDao pssCommTotalDao;
    @Autowired
    WpCommPriDao wpCommPriDao;

    @Autowired
    WpAsciiInfoDao wpAsciiInfoDao;

    @Autowired
    PssCommTotalService pssCommTotalService;

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

        Map<String, Object> result = new HashMap<>();
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
        result.put("minsheng", minsheng);
        return result;
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
     * 首页数据展示
     *
     * @return
     */
    @Override
    public Map<String, Object> firstPageView() {

        Map<String, Object> retMap = new HashMap<>();
        int todayUp = 0;
        int todayDown = 0;
        //当天预警类型占比
        List<PssPriceEwarnEntity> todayMaxPricEwarnList = new ArrayList<>();
        QueryWrapper where1 = new QueryWrapper();
        where1.groupBy("comm_id");
        List<PssPriceEwarnEntity> priceEwarnList = pssPriceEwarnDao.selectList(where1);
        for (PssPriceEwarnEntity entity : priceEwarnList) {
            List<PssPriceEwarnEntity> entities = pssPriceEwarnDao.queryPriceEwarnByCommId(entity.getCommId());
            if (entities == null || entities.size() < 2) {
                continue;
            }
            PssPriceEwarnEntity today = entities.get(0);
            PssPriceEwarnEntity yestday = entities.get(1);
            //统计实时总揽
            // 大于昨天为上涨
            if (today.getPriRange().compareTo(yestday.getPriRange()) == 1) {
                todayUp += 1;
            }
            //小于昨天为下跌 相等不计算
            if (today.getPriRange().compareTo(yestday.getPriRange()) == -1) {
                todayDown += 1;
            }
            todayMaxPricEwarnList.add(entities.get(0));
        }

        List<RateValDto> rateValDtos = new ArrayList<>();
        List<PssPriceEwarnEntity> ewanInfoList = new ArrayList<>();
        for (PssPriceEwarnEntity entity : todayMaxPricEwarnList) {
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

        QueryWrapper where2 = new QueryWrapper();
        where2.eq("data_flag", 0);
        int commTotal = pssCommTotalDao.selectCount(where2);
        //商品总数量
        retMap.put("commTotal", commTotal);
        //最近所有商品一个月涨跌值
        QueryWrapper where3 = new QueryWrapper();
        //一个月前
        where3.between("Date(ewarn_date)", DateUtils.getLastMonthByVal(1), DateUtils.getCurrentDayStr());
//        一年前
//        where3.between("Date(ewarn_date)", DateUtils.getLastYearByVal(1), DateUtils.getCurrentDayStr());
        List<PssPriceEwarnEntity> monthPriRangeList = pssPriceEwarnDao.selectList(where3);
        retMap.put("priVal", monthPriRangeList);
        //今日上涨数量
        retMap.put("totalUp", todayUp);
        //今日下跌数量
        retMap.put("totalDown", todayDown);
        //当前天商品预警类型占比
        retMap.put("rateVel", contionRateVal(rateValDtos));
        //商品预警详情
        retMap.put("ewanInfo", distinctSameSubCommEwarn(ewanInfoList));
        return retMap;
    }

    private List distinctSameSubCommEwarn(List<PssPriceEwarnEntity> ewanInfoList) {
        //用于存放重复的元素的list
//        List<PssPriceEwarnEntity> repeatList = new ArrayList<>();
        for (int i = 0; i < ewanInfoList.size() - 1; i++) {
            for (int j = ewanInfoList.size() - 1; j > i; j--) {
                if (ewanInfoList.get(j).getCommId().equals(ewanInfoList.get(i).getCommId())) {
                    //把相同元素加入list(找出相同的)
//                    repeatList.add(ewanInfoList.get(j));
                    if (ewanInfoList.get(j).getPriRange().compareTo(ewanInfoList.get(i).getPriRange()) == 1) {
                        ewanInfoList.remove(i);//删除重复元素
                    } else {
                        ewanInfoList.remove(j);//删除重复元素
                    }
                }
            }
        }
//        for (PssPriceEwarnEntity entity : repeatList) {
//            System.out.println(entity.getCommId());
//            System.out.println(entity.getPriRange());
//        }
        return ewanInfoList;
    }


    /**
     * 计算二级页面增副和所有价格数据
     *
     * @param commId
     * @return
     */
    @Override
    public Map<String, Object> converZF(Integer commId) {

        Map<String, Object> map = new HashMap<>();

//        QueryWrapper where1 = new QueryWrapper();
//        where1.eq("parent_code", commId);
//        where1.eq("data_flag", 0);
//        List<PssCommTotalEntity> list = pssCommTotalDao.selectList(where1);

        PssPriceEwarnEntity entity = pssPriceEwarnDao.selectMaxRange(commId);
        //计算当天价格
        PssPriceEwarnEntity today = pssPriceEwarnDao.selectMaxDateTimeEntiey(entity.getCommId());
        BigDecimal todayPriValue = today.getPriValue();

        //计算昨天价格
        Date lastDate = DateUtils.addDateDays(today.getEwarnDate(), -1);
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("Date(ewarn_date)", DateUtils.dateToStr(lastDate));
        where2.eq("comm_id", entity.getCommId());
        PssPriceEwarnEntity entity1 = pssPriceEwarnDao.selectOne(where2);
        BigDecimal lastDayPriValue = entity1.getPriValue();

        //计算上月今日价格
        QueryWrapper where4 = new QueryWrapper();
        Date lastMonthDay = DateUtils.addDateDays(today.getEwarnDate(), -30);
        where4.eq("Date(ewarn_date)", DateUtils.dateToStr(lastMonthDay));
        where4.eq("comm_id", entity.getCommId());
        PssPriceEwarnEntity entity2 = pssPriceEwarnDao.selectOne(where4);
        BigDecimal lastMonthTodayPrice = entity2.getPriValue();

        BigDecimal ONE = new BigDecimal(1);
        BigDecimal HUN = new BigDecimal(100);
        //环比
        BigDecimal huanBiTemp = todayPriValue.divide(lastMonthTodayPrice, 2, RoundingMode.HALF_UP).subtract(ONE);
        // 同比
        BigDecimal tongBiTemp = todayPriValue.divide(lastDayPriValue, 2, RoundingMode.HALF_UP).subtract(ONE);
        String huanBi = huanBiTemp.multiply(HUN).toString() + "%";
        String tongBi = tongBiTemp.multiply(HUN).toString() + "%";
        map.put("huanBi", huanBi);
        map.put("tongBi", tongBi);
        //计算所有4类商品价格
        QueryWrapper where3 = new QueryWrapper();
        where3.eq("parent_code", commId.toString());
        where3.eq("data_flag", 0);
        where3.eq("level_code", 3);
        List<PssCommTotalEntity> commList = pssCommTotalDao.selectList(where3);

        List<WpCommPriEntity> priceList = new ArrayList<>();
        for (PssCommTotalEntity comm : commList) {
            WpCommPriEntity pri = wpCommPriDao.selectLastPrice(comm.getCommId());
            priceList.add(pri);
        }
        map.put("allPriceList", priceList);

        if (priceList != null && priceList.size() > 0) {
            map.put("todayPrice", priceList.get(0));
        }
        return map;
    }

    private PssCommTotalEntity getParantCommByCommId(Integer commId) {

        PssCommTotalEntity level_code3Comm = pssCommTotalDao.selectById(commId);

        QueryWrapper where2 = new QueryWrapper();
        where2.eq("comm_id", level_code3Comm.getParentCode());
        where2.eq("level_code", 2);
        PssCommTotalEntity entity = pssCommTotalDao.selectOne(where2);
        return entity;
    }

    //计算商品预警类型占比
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
     * @Desc: 根据1类商品id, 查询4类商品信息，
     * @Param:
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/10/14 16:13
     */
    /*private Map<String, Object> getCommInfoByType_1(PssCommTotalEntity levelCode0) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return null;
        }
        //根据1类查询2类
        QueryWrapper where2 = new QueryWrapper();
        where2.in("parent_code", levelCode0.getCommId());
        where2.eq("data_flag", "0");
        where2.eq("level_code", "1");
        // 获取2类商品
        List<PssCommTotalEntity> commLevelCode1 = baseMapper.selectList(where2);
        int type4CommCount = 0;
        for (PssCommTotalEntity entity1 : commLevelCode1) {
            //根据2类查询3类商品
            QueryWrapper where3 = new QueryWrapper();
            where3.in("parent_code", entity1.getCommId());
            where3.eq("data_flag", "0");
            where3.eq("level_code", "2");
            // 获取3类商品
            List<PssCommTotalEntity> type3CommList = baseMapper.selectList(where3);
            for (PssCommTotalEntity type3Comm : type3CommList) {
                //根据3类商品查询该商品下所有4类商品信息
                QueryWrapper where4 = new QueryWrapper();
                where4.in("parent_code", type3Comm.getCommId());
                where4.eq("data_flag", "0");
                where4.eq("level_code", "3");
                List<PssCommTotalEntity> type4CommList = pssCommTotalDao.selectList(where4);
                type3Comm.setSubCommList(type4CommList);
                //根据3类商品计算基4类商品的价格预警信息，返回此3类商品中涨幅最大的那个4类商品预警信息

            }
            entity1.setSubCommList(type3CommList);
        }
        levelCode0.setSubCommList(commLevelCode1);

        Map<String, Object> map = new HashMap<>();
        map.put("result", levelCode0);
        map.put("totalCount", type4CommCount);
        return map;
    }*/

}
