package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.job.service.CommonJobService;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommConfEntity;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import io.dfjinxin.modules.price.service.PssCommConfService;
import io.dfjinxin.modules.price.service.PssEwarnConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.dfjinxin.modules.price.dao.PssCommConfDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("pssCommConfService")
public class PssCommConfServiceImpl extends ServiceImpl<PssCommConfDao, PssCommConfEntity> implements PssCommConfService {

    @Autowired
    private PssCommConfDao pssCommConfDao;

    @Autowired
    private PssCommTotalDao pssCommTotalDao;
    @Autowired
    private CommonJobService commonJobService;
    @Autowired
    private PssEwarnConfService pssEwarnConfService;

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
    public void saveCommConf(Integer levelCode3Id, List<Integer> ewarnIds, List<Integer> indexIds) {
        if (levelCode3Id == null || ewarnIds == null || indexIds == null) {
            return;
        }

        for (Integer indexId : indexIds) {
            pssCommConfDao.saveCommConf(levelCode3Id, indexId, ewarnIds);
        }
    }

    @Override
    public List<PssCommConfEntity> getCommConfByParms(Integer commId, List<Integer> ewarnIds, List<Integer> indexIds) {
        if (commId == null || ewarnIds == null || indexIds == null) {
            return new ArrayList<>();
        }
        QueryWrapper where = new QueryWrapper();
        where.eq("comm_id", commId);
        where.in("index_id", indexIds);
        where.in("ewarn_id", ewarnIds);
        where.eq("del_flag", 0);
        return pssCommConfDao.selectList(where);
    }

    @Override
    public void saveCommomJob(Integer commId, List<Integer> ewarnIds, List<Integer> indexIds) {
        List<PssCommConfEntity> pcfs = getCommConfByParms(commId, ewarnIds, indexIds);
        for (PssCommConfEntity py : pcfs) {
            PssEwarnConfEntity pe = pssEwarnConfService.queryEntityByEwarnId(py.getEwarnId() + "");
            StringBuilder sb = new StringBuilder();
            sb.append("ewarnId:").append(py.getEwarnId())
                    .append("@")
                    .append("commConfId:").append(py.getConfId());
            commonJobService.saveScheJob(pe.getRschId() + "", "warnTask", sb.toString());
        }


    }

    /**
     * @Desc: 二级页面(预警展示)统计3类商品下指定预警类型的规格品信息
     * @Param: [commId, ewarnTypeId]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssCommConfEntity>
     * @Author: z.h.c
     * @Date: 2019/11/13 17:23
     */
    @Override
    public List<PssCommConfEntity> queryByewarnTypeId(Integer commId, Integer ewarnTypeId) {
        return pssCommConfDao.queryByewarnTypeId(commId, ewarnTypeId);
    }


    @Override
    public void deleteCommConf(Integer confId) {
        if (confId != null) {
            PssCommConfEntity commConfEntity = new PssCommConfEntity();
            commConfEntity.setConfId(confId);
            commConfEntity.setDelFlag(1);
            commConfEntity.setCrteDate(new Date());
            pssCommConfDao.updateById(commConfEntity);
        }
    }


}
