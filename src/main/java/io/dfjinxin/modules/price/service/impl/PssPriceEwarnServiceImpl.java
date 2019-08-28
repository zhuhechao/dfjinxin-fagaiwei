package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.dao.PssPriceEwarnDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("pssPriceEwarnService")
public class PssPriceEwarnServiceImpl extends ServiceImpl<PssPriceEwarnDao, PssPriceEwarnEntity> implements PssPriceEwarnService {

    @Autowired
    PssPriceEwarnDao pssPriceEwarnDao;
    @Autowired
    PssCommTotalDao pssCommTotalDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssPriceEwarnEntity> page = this.page(
                new Query<PssPriceEwarnEntity>().getPage(params),
                new QueryWrapper<PssPriceEwarnEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PssPriceEwarnEntity> queryList() {

        QueryWrapper<PssCommTotalEntity> where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        List<PssCommTotalEntity> pssCommTotalEntities = pssCommTotalDao.selectList(where1);

        List list = new ArrayList<>();
        Map<String, List<PssPriceEwarnEntity>> map;
        for (PssCommTotalEntity entity : pssCommTotalEntities) {
            List<PssPriceEwarnEntity> queryList = pssPriceEwarnDao.queryList(entity.getCommId());
            map = new HashMap<>();
            if ("BC".equals(entity.getCommAbb())) {
                map.put("dazong", queryList);
            } else {
                map.put("minsheng", queryList);
            }
            list.add(map);
        }

        return list;
    }

    @Override
    public List<PssPriceEwarnEntity> queryDetail(Integer commId,Integer ewarnTypeId) {
        QueryWrapper where1 = new QueryWrapper();
        where1.in("comm_id", commId);
        where1.eq("ewarn_type_id", ewarnTypeId);
        return baseMapper.selectList(where1);
    }

}
