package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
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

}