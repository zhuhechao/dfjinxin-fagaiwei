package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexInfoDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wpBaseIndexInfoService")
public class WpBaseIndexInfoServiceImpl extends ServiceImpl<WpBaseIndexInfoDao, WpBaseIndexInfoEntity> implements WpBaseIndexInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpBaseIndexInfoEntity> page = this.page(
                new Query<WpBaseIndexInfoEntity>().getPage(params),
                new QueryWrapper<WpBaseIndexInfoEntity>()
        );

        return new PageUtils(page);
    }

}
