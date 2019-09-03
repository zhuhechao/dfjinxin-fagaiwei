package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssAnalyInfoDao;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;


@Service("pssAnalyInfoService")
public class PssAnalyInfoServiceImpl extends ServiceImpl<PssAnalyInfoDao, PssAnalyInfoEntity> implements PssAnalyInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssAnalyInfoEntity> page = this.page(
                new Query<PssAnalyInfoEntity>().getPage(params),
                new QueryWrapper<PssAnalyInfoEntity>()
        );

        return new PageUtils(page);
    }

}
