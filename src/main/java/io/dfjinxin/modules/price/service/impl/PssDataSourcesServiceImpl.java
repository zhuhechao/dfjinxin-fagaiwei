package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dao.PssDataSourcesDao;
import io.dfjinxin.modules.price.entity.PssDataSourcesEntity;
import io.dfjinxin.modules.price.service.PssDataSourcesService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName DataSourcesServiceImpl
 * @Author：lym 863968235@qq.com
 * @Date： 2019/9/19 17:14
 * 修改备注：
 */
@Service("dataSourcesService")
public class PssDataSourcesServiceImpl
        extends ServiceImpl<PssDataSourcesDao, PssDataSourcesEntity>
        implements PssDataSourcesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }



}
