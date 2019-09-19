package io.dfjinxin.modules.report.service.impl;

import io.dfjinxin.modules.report.dto.PssRptConfDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.report.dao.PssRptConfDao;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;


@Service("pssRptConfService")
public class PssRptConfServiceImpl extends ServiceImpl<PssRptConfDao, PssRptConfEntity> implements PssRptConfService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRptConfEntity> page = this.page(
                new Query<PssRptConfEntity>().getPage(params),
                new QueryWrapper<PssRptConfEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public PssRptConfDto saveOrUpdate(PssRptConfDto dto) {
        PssRptConfEntity entity = PssRptConfEntity.toEntity(dto);

        super.saveOrUpdate(entity);
        return PssRptConfEntity.toData(entity);
    }

}