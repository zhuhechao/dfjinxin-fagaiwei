package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommConfDao;
import io.dfjinxin.modules.price.entity.PssCommConfEntity;
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
    public void saveCommConf(Map<String, List<Integer>> params) {
        if (params.containsKey("commIds") && params.containsKey("ewarnIds")) {
            List<Integer> commIds = params.get("commIds");
            List<Integer> ewarnIds = params.get("ewarnIds");
            for (Integer commId : commIds) {
                pssCommConfDao.saveCommConf(commId, ewarnIds);
            }
        }
    }

}
