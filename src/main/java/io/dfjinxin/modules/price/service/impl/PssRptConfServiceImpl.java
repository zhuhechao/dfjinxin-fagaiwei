package io.dfjinxin.modules.price.service.impl;

import io.dfjinxin.modules.price.dto.PssRptConfDto;
import io.dfjinxin.modules.price.dto.PssRptInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.entity.PssRptInfoEntity;
import io.dfjinxin.modules.price.service.PssRptInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssRptConfDao;
import io.dfjinxin.modules.price.entity.PssRptConfEntity;
import io.dfjinxin.modules.price.service.PssRptConfService;


@Service("pssRptConfService")
public class PssRptConfServiceImpl extends ServiceImpl<PssRptConfDao, PssRptConfEntity> implements PssRptConfService {

    @Autowired
    private PssRptInfoService pssRptInfoService;

    @Override
    public PssRptConfDto saveOrUpdate(PssRptConfDto dto) {
        PssRptConfEntity entity = PssRptConfEntity.toEntity(dto);

        super.saveOrUpdate(entity);

        PssRptInfoDto infoDto = new PssRptInfoDto()
                .setCommId(entity.getCommId())
                .setRptId(entity.getRptId())
                .setRptFile(null)
                .setRptFreq(entity.getRptFreq())
                .setRptName(entity.getRptName())
                .setRptType(entity.getRptType())
                .setRptDate(new Date())
                .setRptPath(entity.getRptPath())
                .setCrteTime(new Date())
                .setStatCode(entity.getStatCode())
                .setDelTime(null);

        pssRptInfoService.saveOrUpdate(infoDto);

        return PssRptConfEntity.toData(entity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRptConfEntity> page = this.page(
                new Query<PssRptConfEntity>().getPage(params),
                new QueryWrapper<PssRptConfEntity>()
        );

        return new PageUtils(page);
    }

}
