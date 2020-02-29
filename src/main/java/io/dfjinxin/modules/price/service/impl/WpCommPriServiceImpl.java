package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.WpCommPriDao;
import io.dfjinxin.modules.price.entity.WpCommPriEntity;
import io.dfjinxin.modules.price.service.WpCommPriService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("wpCommPriService")
public class WpCommPriServiceImpl extends ServiceImpl<WpCommPriDao, WpCommPriEntity> implements WpCommPriService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpCommPriEntity> page = this.page(
                new Query<WpCommPriEntity>().getPage(params),
                new QueryWrapper<WpCommPriEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpCommPriEntity> getData(Map<String, Object> params) {
        String indexId=params.get("indexId").toString();
        Date startDate= (Date) params.get("startDate");
        Date endDate= (Date) params.get("endDate");
        QueryWrapper  queryWrapper=new QueryWrapper<WpCommPriEntity>();
        queryWrapper.eq("index_id",indexId);
        queryWrapper.between("data_time",startDate,endDate);
        queryWrapper.orderByDesc(new String[]{"data_time"});
        return baseMapper.selectList(queryWrapper);
    }

}
