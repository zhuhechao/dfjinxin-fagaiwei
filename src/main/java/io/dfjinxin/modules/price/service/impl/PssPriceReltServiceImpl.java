package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.dto.PriceReltLineChartsDto;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.price.dao.PssPriceReltDao;
import io.dfjinxin.modules.price.dto.PriceReltDto;
import io.dfjinxin.modules.price.dto.PssPriceReltDto;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("pssPriceReltService")
public class PssPriceReltServiceImpl extends ServiceImpl<PssPriceReltDao, PssPriceReltEntity> implements PssPriceReltService {

    @Autowired
    private WpBaseIndexValDao wpBaseIndexValDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }

    @Override
    public PageUtils getDataGrid(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.getDataGrid(page, params);
        return new PageUtils(page);
    }

    private WpBaseIndexValEntity getIndexValByIndexId(PssPriceReltEntity entity) {
        QueryWrapper<WpBaseIndexValEntity> where = new QueryWrapper();
        where.eq("comm_id", entity.getCommId());
        where.eq("index_id", entity.getIndexId());
        where.eq("date", entity.getDataDate());
        return wpBaseIndexValDao.selectOne(where);
    }

    @Override
    public Map<String, Object> detail(Long id,String foreType, String startDate, String endDate) {

        PssPriceReltEntity entity = baseMapper.selectById(id);
        if (entity == null) return null;

        Map<String, Object> map = new HashMap<>();
        PriceReltDto dto = new PriceReltDto();
        dto.setId(entity.getId());
        dto.setDataDate(entity.getDataDate());
        dto.setReviDate(entity.getReviTime());
        dto.setForePrice(entity.getForePrice());
        dto.setReviPrice(entity.getReviPrice());
        WpBaseIndexValEntity indexValEntity = this.getIndexValByIndexId(entity);
        dto.setRealPrice(indexValEntity != null ? new BigDecimal(indexValEntity.getValue()) : new BigDecimal(0));
        //表格数据
        map.put("dataGrid", dto);
        //折线图根据频度统计
//        String foreType = entity.getForeType();
        List<PssPriceReltEntity> reltEntities = this.jiaGeYuCeLineCharts(foreType, entity.getCommId(), startDate, endDate);
        Map<String, Object> lineChartsMap = convertLineCharts(reltEntities);
        if (!lineChartsMap.isEmpty()) {
            map.put("lineCharts", reltEntities);
        }
        return map;
    }

    private Map<String, Object> convertLineCharts(List<PssPriceReltEntity> list) {
        //实际
        List<PriceReltLineChartsDto> shiJi = new ArrayList<>();
        //预测
        List<PriceReltLineChartsDto> xuCe = new ArrayList<>();
        //修正
        List<PriceReltLineChartsDto> xiuZheng = new ArrayList<>();
        list.forEach(entity -> {
            PriceReltLineChartsDto dto = new PriceReltLineChartsDto();
            dto.setCommId(entity.getCommId());
            dto.setDate(entity.getDataDate());
            WpBaseIndexValEntity indexValEntity = this.getIndexValByIndexId(entity);
            dto.setVal(indexValEntity != null ? new BigDecimal(indexValEntity.getValue()) : new BigDecimal(0));
            shiJi.add(dto);
            dto.setVal(entity.getForePrice());
            xuCe.add(dto);
            dto.setVal(entity.getReviPrice());
            xiuZheng.add(dto);
        });
        Map<String, Object> map = new HashMap<>();
        if (shiJi != null && shiJi.size() > 0) map.put("shiJi", shiJi);
        if (xuCe != null && xuCe.size() > 0) map.put("xuCe", xuCe);
        if (xiuZheng != null && xiuZheng.size() > 0) map.put("xiuZheng", xiuZheng);
        return map;
    }

    @Override
    public PssPriceReltDto queryCommByCommId(Integer commId) {
        return baseMapper.queryCommByCommId(commId);
    }

    @Override
    public Map<String, Object> getLineCharts(Map<String, Object> params) {
        Integer commId = params.containsKey("commId") ? (Integer) params.get("commId") : null;
        PssPriceReltDto pssPriceReltDto = this.queryCommByCommId(commId);
        if (pssPriceReltDto == null) {
            return null;
        }
        String startDate = params.containsKey("dateFrom") ? (String) params.get("dateFrom") : null;
        String endDate = params.containsKey("dateTo") ? (String) params.get("dateTo") : null;
        String foreType = pssPriceReltDto.getForeType();
        List<PssPriceReltEntity> list = this.jiaGeYuCeLineCharts(foreType, pssPriceReltDto.getCommId(), startDate, endDate);
        Map<String, Object> lineChartsMap = this.convertLineCharts(list);
        return lineChartsMap.isEmpty() ? null : lineChartsMap;
    }

    private List<PssPriceReltEntity> jiaGeYuCeLineCharts(String foreType, Integer commId, String startDate, String
            endDate) {

        //当天日期
        String todayStr = DateUtils.dateToStr(new Date());
        //本周最后一天
        String weekLastDayStr = DateUtils.getWeekLastDayStr();
        //本月最后一天
        String monthLastDayStr = DateUtils.getMonthLastDayStr();

        //预测类型-日、周、月
        //周预测-统计本周之后的4周数据
        List<PssPriceReltEntity> list = new ArrayList<>();

        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
            list = baseMapper.selectByDate(commId, foreType, startDate, endDate);
        } else {
            if ("周预测".equals(foreType)) {
                //本周是后一天
                list = baseMapper.selectByForeType(commId, foreType, weekLastDayStr, 4);
            }
            //日预测-统计30天
            if ("日预测".equals(foreType)) {
                list = baseMapper.selectByForeType(commId, foreType, todayStr, 30);
            }
            //月预测-统计当前月之后的12个月数据
            if ("月预测".equals(foreType)) {
                list = baseMapper.selectByForeType(commId, foreType, monthLastDayStr, 12);
            }
        }
        return list;
    }

}
