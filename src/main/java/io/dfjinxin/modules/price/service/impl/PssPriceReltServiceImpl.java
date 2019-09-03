package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssPriceReltDao;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;


@Service("pssPriceReltService")
public class PssPriceReltServiceImpl extends ServiceImpl<PssPriceReltDao, PssPriceReltEntity> implements PssPriceReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssPriceReltEntity> page = this.page(
                new Query<PssPriceReltEntity>().getPage(params),
                new QueryWrapper<PssPriceReltEntity>()
        );

        return new PageUtils(page);
    }

}
