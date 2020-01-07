package io.dfjinxin.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;

import io.dfjinxin.modules.report.dao.PssRptInfoDao;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;

import javax.annotation.Resource;


@Service("pssRptInfoService")
public class PssRptInfoServiceImpl extends ServiceImpl<PssRptInfoDao, PssRptInfoEntity> implements PssRptInfoService {


    @Resource
    private PssRptInfoDao pssRptInfoDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }


    /**
     * 分析报告单独接口
     * @return
     */
    @Override
    public List<PssRptInfoEntity> queryRptName(Map<String,Object> params) {
             return pssRptInfoDao.queryRptName(params);
    }


}