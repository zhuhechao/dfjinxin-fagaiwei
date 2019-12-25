package io.dfjinxin.modules.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.model.dao.ModelInfoDao;
import io.dfjinxin.modules.model.entity.ModelInfoEntity;
import io.dfjinxin.modules.model.service.ModelInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("modelInfoService")
public class ModelInfoServiceImpl extends ServiceImpl<ModelInfoDao, ModelInfoEntity> implements ModelInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ModelInfoEntity> page = this.page(
                new Query<ModelInfoEntity>().getPage(params),
                new QueryWrapper<ModelInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryByPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = super.baseMapper.queryByPage(page, params);
        return new PageUtils(page);
    }

    @Override
    public List<String> getAlgorithm() {
        QueryWrapper<ModelInfoEntity> where = new QueryWrapper();
        where.select("algorithm");
        where.eq("is_show", 1);
        where.groupBy("algorithm");
        List<ModelInfoEntity> list = baseMapper.selectList(where);

        List<String> algorithmList = new ArrayList<>();
        list.forEach(entity -> algorithmList.add(entity.getAlgorithm()));
        return algorithmList;
    }

}
