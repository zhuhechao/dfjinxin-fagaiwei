package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpCommIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpCommIndexValService;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("wpCommIndexValService")
public class WpCommIndexValServiceImpl extends ServiceImpl<WpCommIndexValDao, WpCommIndexValEntity> implements WpCommIndexValService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;
    @Autowired
    private WpCommIndexValDao wpCommIndexValDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpCommIndexValEntity> page = this.page(
                new Query<WpCommIndexValEntity>().getPage(params),
                new QueryWrapper<WpCommIndexValEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Map<String, PssCommTotalEntity>> queryList() {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        //查询0级商品
        List<PssCommTotalEntity> commType0 = pssCommTotalDao.selectList(where1);

        List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
        Map<String, PssCommTotalEntity> tempMap = new HashMap<>();
        for (PssCommTotalEntity entity : commType0) {
            Map<String, Object> temp = queryCommByLevelCode0(entity);
            PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
            if ("BC".equals(entity.getCommAbb())) {
                tempMap.put("dazong", result);
            } else {
                tempMap.put("minsheng", result);
            }
        }
        resultList.add(tempMap);
        return resultList;
    }

    @Override
    public Map<String, Object> queryDetailByCommId(Integer commId) {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("comm_id",commId);
        //where1.eq()

        return null;
    }

    @Override
    public List<Map<String, Object>> queryIndexTypeByCommId(Integer commId) {
        return wpCommIndexValDao.queryIndexTypeByCommId(commId);
    }

    private Map<String, Object> queryCommByLevelCode0(PssCommTotalEntity levelCode0) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return null;
        }
        //根据0类查询1类
        QueryWrapper where1 = new QueryWrapper();
        where1.in("parent_code", levelCode0.getCommId());
        where1.eq("data_flag", "0");
        where1.eq("level_code", "1");
        // 获取一类商品
        List<PssCommTotalEntity> commLevelCode1 = pssCommTotalDao.selectList(where1);

        QueryWrapper where2 = new QueryWrapper();
        where2.eq("data_flag", "0");
        where2.eq("level_code", "2");
        for (PssCommTotalEntity entity1 : commLevelCode1) {
            where2.eq("parent_code", entity1.getCommId());
            List<PssCommTotalEntity> commLevelCode2 = pssCommTotalDao.selectList(where2);
            entity1.setSubCommList(commLevelCode2);
        }
        levelCode0.setSubCommList(commLevelCode1);
        Map<String, Object> map = new HashMap<>();
        map.put("result", levelCode0);
        return map;
    }

}
