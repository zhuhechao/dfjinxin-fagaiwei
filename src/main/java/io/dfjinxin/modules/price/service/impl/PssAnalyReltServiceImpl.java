package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssAnalyReltDao;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("pssAnalyReltService")
public class PssAnalyReltServiceImpl extends ServiceImpl<PssAnalyReltDao, PssAnalyReltEntity> implements PssAnalyReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssAnalyReltEntity> page = this.page(
                new Query<PssAnalyReltEntity>().getPage(params)
        );
        page.setRecords(this.baseMapper.queryPage(params));
        return new PageUtils(page);
    }

}
