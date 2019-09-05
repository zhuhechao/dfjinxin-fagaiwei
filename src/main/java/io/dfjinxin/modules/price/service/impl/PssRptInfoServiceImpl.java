package io.dfjinxin.modules.price.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssRptInfoDao;
import io.dfjinxin.modules.price.entity.PssRptInfoEntity;
import io.dfjinxin.modules.price.service.PssRptInfoService;


@Service("pssRptInfoService")
public class PssRptInfoServiceImpl extends ServiceImpl<PssRptInfoDao, PssRptInfoEntity> implements PssRptInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRptInfoEntity> page = this.page(
                new Query<PssRptInfoEntity>().getPage(params),
                new QueryWrapper<PssRptInfoEntity>()
        );

        return new PageUtils(page);
    }

}
