package io.dfjinxin.modules.report.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.report.dao.PssRptInfoDao;
import io.dfjinxin.modules.report.dao.WpCarwlerDataDao;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.entity.WpCarwlerDataEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.modules.report.service.WpCarwlerDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("wpCarwlerDataService")
public class WpCarwlerDataServiceImpl extends ServiceImpl<WpCarwlerDataDao, WpCarwlerDataEntity> implements WpCarwlerDataService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long no = params.containsKey("pageIndex") ? Long.valueOf(params.get("pageIndex").toString()) : 1;
        Long limit = params.containsKey("pageSize") ? Long.valueOf(params.get("pageSize").toString()) : 10;
        IPage<Map<String, Object>> page =  baseMapper.queryPage(new Page<>(no, limit), params);
        return new PageUtils(page);
    }
}