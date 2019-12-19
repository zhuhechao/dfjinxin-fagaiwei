package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpPubOmDao;
import io.dfjinxin.modules.analyse.dto.CommYuQingDto;
import io.dfjinxin.modules.analyse.entity.WpPubOmEntity;
import io.dfjinxin.modules.analyse.service.WpPubOmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("wpPubOmService")
public class WpPubOmServiceImpl extends ServiceImpl<WpPubOmDao, WpPubOmEntity> implements WpPubOmService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpPubOmEntity> page = this.page(
                new Query<WpPubOmEntity>().getPage(params),
                new QueryWrapper<WpPubOmEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, Object> getYuQing(Integer commId, String dateFrom, String dateTo) {

        QueryWrapper<WpPubOmEntity> where = new QueryWrapper<>();
        where.eq("comm_id", commId);
        where.orderByAsc("data_date");
        if (StringUtils.isNotEmpty(dateFrom) && StringUtils.isNotEmpty(dateTo)) {
            where.between("data_date", dateFrom, dateTo);
        } else {
            //默认取一个月数据
            String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));
            String last30DayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -30));
            where.between("data_date",last30DayStr, lastDayStr);
        }
        List<WpPubOmEntity> entities = baseMapper.selectList(where);

        //中立
        List<CommYuQingDto> zhongLi = new ArrayList<>();
        List<CommYuQingDto> zhengMian = new ArrayList<>();
        List<CommYuQingDto> fuMian = new ArrayList<>();
        entities.forEach(entity -> {
            CommYuQingDto dto = new CommYuQingDto();
            dto.setCommId(entity.getCommId());
            dto.setDate(entity.getDataDate());
            dto.setHeatTrend(entity.getHeatTrend());
            dto.setVal(entity.getNeuTrend());
            zhongLi.add(dto);
            dto.setVal(entity.getPosTrend());
            zhengMian.add(dto);
            dto.setVal(entity.getSentimentTrend());
            fuMian.add(dto);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("zhongLi", zhongLi);
        map.put("zhengMian", zhengMian);
        map.put("fuMian", fuMian);
        return map;
    }

}
