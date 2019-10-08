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
import java.text.DecimalFormat;
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

    @Override
    public List<PssPriceEwarnEntity> queryList() {

        QueryWrapper<PssCommTotalEntity> where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        List<PssCommTotalEntity> pssCommTotalEntities = pssCommTotalDao.selectList(where1);

        List list = new ArrayList<>();
        Map<String, List<PssPriceEwarnEntity>> map;
        for (PssCommTotalEntity entity : pssCommTotalEntities) {
            List<PssPriceEwarnEntity> queryList = pssPriceEwarnDao.queryList(entity.getCommId());
            map = new HashMap<>();
            if (1 == entity.getCommId()) {
                map.put("dazong", queryList);
            } else {
                map.put("minsheng", queryList);
            }
            list.add(map);
        }

        return list;
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

    @Override
    public Map<String, Object> queryType3Warn() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List todayUp = new ArrayList();
        List todayDown = new ArrayList();
        List<PssPriceEwarnEntity> pssPriceEwarnEntityList = pssPriceEwarnDao.queryType3Warn();
        List<RateValDto> rateValDtos = new ArrayList<>();
        for (PssPriceEwarnEntity entity : pssPriceEwarnEntityList) {
            WpAsciiInfoEntity asciiInfoEntity = wpAsciiInfoDao.selectById(entity.getEwarnLevel());
            entity.setEwarnLevel(asciiInfoEntity.getCodeName());
            //统计实时总揽
            if (entity.getPriRange().compareTo(BigDecimal.ZERO) == 1) {
                todayUp.add(entity);
            }
            if (entity.getPriRange().compareTo(BigDecimal.ZERO) == -1) {
                todayDown.add(entity);
            }

            RateValDto rateValDto = new RateValDto();
            rateValDto.setEwanName(asciiInfoEntity.getCodeName());
            rateValDto.setEwarnLevel(entity.getEwarnLevel());
            rateValDtos.add(rateValDto);

        }
        //商品预警详情
        map.put("ewanInfo", pssPriceEwarnEntityList);
        QueryWrapper where = new QueryWrapper();
        where.eq("data_flag", 0);
        int commTotal = pssCommTotalDao.selectCount(where);
        //商品总数量
        map.put("commTotal", commTotal);
        //今日上涨数量
        map.put("totalUp", todayUp.size());
        //今日下跌数量
        map.put("totalDown", todayDown.size());
        //商品预警类型占比
//        contionRateVal(rateValDtos);
        map.put("rateVel", contionRateVal(rateValDtos));

        //商品最近一个月涨跌值
        QueryWrapper where2 = new QueryWrapper();
        where2.between("Date(ewarn_date)", DateUtils.getLastMonthByVal(1), DateUtils.getCurrentDayStr());
        List<PssPriceEwarnEntity> priValList = pssPriceEwarnDao.selectList(where2);
        map.put("priVal", priValList);
        return map;
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

}
