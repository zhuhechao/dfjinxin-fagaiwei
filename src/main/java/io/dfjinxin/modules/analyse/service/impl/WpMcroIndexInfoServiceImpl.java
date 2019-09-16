package io.dfjinxin.modules.analyse.service.impl;

import io.dfjinxin.modules.analyse.dao.WpMcroIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private WpMcroIndexValDao wpMcroIndexValDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpMcroIndexInfoEntity> page = this.page(
                new Query<WpMcroIndexInfoEntity>().getPage(params),
                new QueryWrapper<WpMcroIndexInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpMcroIndexInfoEntity> getName() {
        return wpMcroIndexInfoDao.selectIndexName();
    }

    @Override
    public PageUtils queryByPage(String indexId, String dateFrom, String dateTo, String areaCodes) {
        QueryWrapper where = new QueryWrapper();
        where.eq("index_id",indexId);
        if(StringUtils.isNotBlank(dateFrom)&& StringUtils.isNotBlank(dateTo)){
            where.between("data_time",dateFrom,dateTo);
        }else {
            where.between("data_time",dateFrom,dateTo);
        }
        if(StringUtils.isNotBlank(areaCodes)){
            where.between("stat_area_id",dateFrom,dateTo);
        }
        List<WpMcroIndexValEntity> list = wpMcroIndexValDao.selectList(where);
        return null;
    }

}
