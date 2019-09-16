package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommConfDao;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommConfEntity;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("pssCommConfService")
public class PssCommConfServiceImpl extends ServiceImpl<PssCommConfDao, PssCommConfEntity> implements PssCommConfService {

    @Autowired
    private PssCommConfDao pssCommConfDao;

    @Autowired
    private PssCommTotalDao pssCommTotalDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssCommConfEntity> page = this.page(
                new Query<PssCommConfEntity>().getPage(params),
                new QueryWrapper<PssCommConfEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCommConf(Integer levelCode2Id, List<Integer> ewarnIds) {
        if (levelCode2Id == null || ewarnIds == null) {
            return;
        }
        QueryWrapper where = new QueryWrapper();
        where.eq("parent_code", levelCode2Id);
        where.eq("level_code", 3);
        where.eq("data_flag", 0);
        List<PssCommTotalEntity> levelCode3Ids = pssCommTotalDao.selectList(where);
        for (PssCommTotalEntity entity : levelCode3Ids) {
            pssCommConfDao.saveCommConf(entity.getCommId(), ewarnIds);
        }
    }
}
