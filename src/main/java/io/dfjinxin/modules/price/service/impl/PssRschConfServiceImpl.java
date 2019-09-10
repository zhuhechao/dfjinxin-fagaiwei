package io.dfjinxin.modules.price.service.impl;

import io.dfjinxin.modules.price.dto.PssRschConfDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssRschConfDao;
import io.dfjinxin.modules.price.entity.PssRschConfEntity;
import io.dfjinxin.modules.price.service.PssRschConfService;


@Service("pssRschConfService")
public class PssRschConfServiceImpl extends ServiceImpl<PssRschConfDao, PssRschConfEntity> implements PssRschConfService {


    @Override
    public PssRschConfDto saveOrUpdate(PssRschConfDto dto) {
        PssRschConfEntity entity = PssRschConfEntity.toEntity(dto);

        super.saveOrUpdate(entity);
        return PssRschConfEntity.toData(entity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRschConfEntity> page = this.page(
                new Query<PssRschConfEntity>().getPage(params),
                new QueryWrapper<PssRschConfEntity>()
        );

        return new PageUtils(page);
    }

}
