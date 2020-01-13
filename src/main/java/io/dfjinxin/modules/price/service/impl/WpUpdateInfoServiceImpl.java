package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.WpUpdateInfoDao;
import io.dfjinxin.modules.price.entity.WpUpdateInfoEntity;
import io.dfjinxin.modules.price.service.WpUpdateInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wpUpdateInfoService")
public class WpUpdateInfoServiceImpl extends ServiceImpl<WpUpdateInfoDao, WpUpdateInfoEntity> implements WpUpdateInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpUpdateInfoEntity> page = this.page(
                new Query<WpUpdateInfoEntity>().getPage(params),
                new QueryWrapper<WpUpdateInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Long getEverydayInfoTotal() {
        return baseMapper.getEverydayInfoTotal(DateUtils.getCurrentDayStr());
    }

}
