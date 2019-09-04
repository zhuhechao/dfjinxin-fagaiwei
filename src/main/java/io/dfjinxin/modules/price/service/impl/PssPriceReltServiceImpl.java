package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dao.PssPriceReltDao;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("pssPriceReltService")
public class PssPriceReltServiceImpl extends ServiceImpl<PssPriceReltDao, PssPriceReltEntity> implements PssPriceReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }

}
