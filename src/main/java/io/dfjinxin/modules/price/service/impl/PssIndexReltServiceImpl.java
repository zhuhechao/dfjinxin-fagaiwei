package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.price.dao.PssIndexReltDao;
import io.dfjinxin.modules.price.dto.PssIndexReltDto;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import io.dfjinxin.modules.price.service.PssIndexReltService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("pssIndexReltService")
public class PssIndexReltServiceImpl extends ServiceImpl<PssIndexReltDao, PssIndexReltEntity> implements PssIndexReltService {

    @Override
    public List<PssIndexReltDto> list(String indexName, Date dateFrom, Date dateTo) {
        return super.baseMapper.list(indexName, dateFrom, dateTo);
    }

}
