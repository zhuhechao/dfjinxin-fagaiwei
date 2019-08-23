package io.dfjinxin.modules.price.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;


@Service("pssCommTotalService")
public class PssCommTotalServiceImpl extends ServiceImpl<PssCommTotalDao, PssCommTotalEntity> implements PssCommTotalService {

//    @Autowired
//    private PssCommTotalDao pssCommTotalDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssCommTotalEntity> page = this.page(
                new Query<PssCommTotalEntity>().getPage(params),
                new QueryWrapper<PssCommTotalEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String,List<PssCommTotalEntity>> queryCommType() {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");

        List<PssCommTotalEntity> commType1 = baseMapper.selectList(where1);
        List<PssCommTotalEntity> commType2 = new ArrayList<>();
        List<PssCommTotalEntity> commType3 = new ArrayList<>();
        Map<String, List<PssCommTotalEntity>> resultMap = new HashMap<>();
        for (PssCommTotalEntity entity : commType1) {
            QueryWrapper where2 = new QueryWrapper();
            where2.in("parent_code", entity.getCommId());
            where2.eq("data_flag", "0");
            where2.eq("level_code", "1");
            List<PssCommTotalEntity> subType = baseMapper.selectList(where2);
            commType2.addAll(subType);
        }

        for(PssCommTotalEntity entity :commType2){
            QueryWrapper where3 = new QueryWrapper();
            where3.in("parent_code", entity.getCommId());
            where3.eq("data_flag", "0");
            where3.eq("level_code", "2");
            List<PssCommTotalEntity> subType3 = baseMapper.selectList(where3);
            commType3.addAll(subType3);
        }

        resultMap.put("commType1",commType1);
        resultMap.put("commType2",commType2);
        resultMap.put("commType3",commType3);
        return resultMap;
    }

}
