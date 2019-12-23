package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dao.PssAnalyReltDao;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("pssAnalyReltService")
public class PssAnalyReltServiceImpl extends ServiceImpl<PssAnalyReltDao, PssAnalyReltEntity> implements PssAnalyReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }

    @Autowired
    private PssAnalyReltDao pssAnalyReltDao;

    @Override
    public List<PssAnalyReltEntity> getList(Map<String, Object> params ) {
        return pssAnalyReltDao.getList(params);
    }

    @Override
    public PssAnalyReltEntity selectByAnalyId(Integer analyId) {
        return baseMapper.selectByAnalyId(analyId);
    }
}
