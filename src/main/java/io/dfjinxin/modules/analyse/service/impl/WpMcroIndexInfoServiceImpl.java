package io.dfjinxin.modules.analyse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.analyse.dao.WpMcroIndexInfoDao;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpMcroIndexInfoService;


@Service("wpMcroIndexInfoService")
public class WpMcroIndexInfoServiceImpl extends ServiceImpl<WpMcroIndexInfoDao, WpMcroIndexInfoEntity> implements WpMcroIndexInfoService {

    @Autowired
    private WpMcroIndexInfoDao wpMcroIndexInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpMcroIndexInfoEntity> page = this.page(
                new Query<WpMcroIndexInfoEntity>().getPage(params),
                new QueryWrapper<WpMcroIndexInfoEntity>()
        );

        return new PageUtils(page);
    }

//    TODO
    @Override
    public WpMcroIndexInfoEntity getType() {
        return null;
    }

    @Override
    public List<WpMcroIndexInfoEntity> getName() {
        return wpMcroIndexInfoDao.selectIndexName();
    }

}
