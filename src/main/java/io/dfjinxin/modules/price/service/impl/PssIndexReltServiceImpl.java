package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssIndexReltDao;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import io.dfjinxin.modules.price.service.PssIndexReltService;


@Service("pssIndexReltService")
public class PssIndexReltServiceImpl extends ServiceImpl<PssIndexReltDao, PssIndexReltEntity> implements PssIndexReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssIndexReltEntity> page = this.page(
                new Query<PssIndexReltEntity>().getPage(params),
                new QueryWrapper<PssIndexReltEntity>()
        );

        return new PageUtils(page);
    }

}
