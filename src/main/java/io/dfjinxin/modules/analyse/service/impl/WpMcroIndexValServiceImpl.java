package io.dfjinxin.modules.analyse.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.analyse.dao.WpMcroIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpMcroIndexValService;


@Service("wpMcroIndexValService")
public class WpMcroIndexValServiceImpl extends ServiceImpl<WpMcroIndexValDao, WpMcroIndexValEntity> implements WpMcroIndexValService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpMcroIndexValEntity> page = this.page(
                new Query<WpMcroIndexValEntity>().getPage(params),
                new QueryWrapper<WpMcroIndexValEntity>()
        );

        return new PageUtils(page);
    }

}