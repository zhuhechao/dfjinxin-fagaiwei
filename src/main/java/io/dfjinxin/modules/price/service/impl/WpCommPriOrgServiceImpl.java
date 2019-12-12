package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.WpCommPriOrgDao;
import io.dfjinxin.modules.price.entity.WpCommPriOrgEntity;
import io.dfjinxin.modules.price.service.WpCommPriOrgService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WpCommPriOrgServiceImpl
 * @Author：lym 863968235@qq.com
 * @Date： 2019/12/10 16:42
 * 修改备注：
 */
@Service("wpCommPriOrgService")
public class WpCommPriOrgServiceImpl extends ServiceImpl<WpCommPriOrgDao, WpCommPriOrgEntity> implements WpCommPriOrgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpCommPriOrgEntity> page = this.page(
                new Query<WpCommPriOrgEntity>().getPage(params),
                new QueryWrapper<WpCommPriOrgEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpCommPriOrgEntity> getData(Map<String, Object> params) {
        String indexId=params.get("indexId").toString();
        Date startDate= (Date) params.get("startDate");
        Date endDate= (Date) params.get("endDate");
        QueryWrapper  queryWrapper=new QueryWrapper<WpCommPriOrgEntity>();
        queryWrapper.eq("index_id",indexId);
        queryWrapper.between("data_time",startDate,endDate);
        queryWrapper.orderByDesc(new String[]{"data_time"});
        return baseMapper.selectList(queryWrapper);
    }
}
