package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssRrunInfoDao;
import io.dfjinxin.modules.price.entity.PssRrunInfoEntity;
import io.dfjinxin.modules.price.service.PssRrunInfoService;


@Service("pssRrunInfoService")
public class PssRrunInfoServiceImpl extends ServiceImpl<PssRrunInfoDao, PssRrunInfoEntity> implements PssRrunInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRrunInfoEntity> page = this.page(
                new Query<PssRrunInfoEntity>().getPage(params),
                new QueryWrapper<PssRrunInfoEntity>()
        );

        return new PageUtils(page);
    }

}
