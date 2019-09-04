package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssForeModDao;
import io.dfjinxin.modules.price.entity.PssForeModEntity;
import io.dfjinxin.modules.price.service.PssForeModService;


@Service("pssForeModService")
public class PssForeModServiceImpl extends ServiceImpl<PssForeModDao, PssForeModEntity> implements PssForeModService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssForeModEntity> page = this.page(
                new Query<PssForeModEntity>().getPage(params),
                new QueryWrapper<PssForeModEntity>()
        );

        return new PageUtils(page);
    }

}
