package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssForeModResultDao;
import io.dfjinxin.modules.price.entity.PssForeModResultEntity;
import io.dfjinxin.modules.price.service.PssForeModResultService;


@Service("pssForeModResultService")
public class PssForeModResultServiceImpl extends ServiceImpl<PssForeModResultDao, PssForeModResultEntity> implements PssForeModResultService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssForeModResultEntity> page = this.page(
                new Query<PssForeModResultEntity>().getPage(params),
                new QueryWrapper<PssForeModResultEntity>()
        );

        return new PageUtils(page);
    }

}
