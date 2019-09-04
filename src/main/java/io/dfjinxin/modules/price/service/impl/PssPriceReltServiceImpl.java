package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssPriceReltDao;
import io.dfjinxin.modules.price.dto.PssPriceReltDto;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("pssPriceReltService")
public class PssPriceReltServiceImpl extends ServiceImpl<PssPriceReltDao, PssPriceReltEntity> implements PssPriceReltService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage page = this.page(
                new Query<PssPriceReltEntity>().getPage(params)
        );
        List<PssPriceReltEntity> list = this.baseMapper.queryPage(params);
        List<PssPriceReltDto> dtos = new ArrayList();

        for(PssPriceReltEntity entity:list) {
            dtos.add(PssPriceReltEntity.toData(entity));
        }
        page.setRecords(dtos);
        return new PageUtils(page);
    }

}
