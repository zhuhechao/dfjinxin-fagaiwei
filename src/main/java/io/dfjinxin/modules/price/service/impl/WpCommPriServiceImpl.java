package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.WpCommPriDao;
import io.dfjinxin.modules.price.entity.WpCommPriEntity;
import io.dfjinxin.modules.price.service.WpCommPriService;


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
        String commid=params.get("commId").toString();
        Date startDate= (Date) params.get("startDate");
        Date endDate= (Date) params.get("endDate");
        String priType=  params.get("priType")+"";
        QueryWrapper  queryWrapper=new QueryWrapper<WpCommPriEntity>();
        queryWrapper.eq("comm_id",commid);
        queryWrapper.eq("pri_type",priType);
        queryWrapper.between("data_time",startDate,endDate);
        queryWrapper.orderByDesc(new String[]{"data_time"});
        return baseMapper.selectList(queryWrapper);
    }

}