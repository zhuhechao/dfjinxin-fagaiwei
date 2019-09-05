package io.dfjinxin.modules.price.service.impl;

import io.dfjinxin.modules.price.dto.PssIndexReltDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssIndexReltDao;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import io.dfjinxin.modules.price.service.PssIndexReltService;


@Service("pssIndexReltService")
public class PssIndexReltServiceImpl extends ServiceImpl<PssIndexReltDao, PssIndexReltEntity> implements PssIndexReltService {

    @Override
    public List<PssIndexReltDto> list(String indexName, Date dateFrom, Date dateTo) {
        return super.baseMapper.list(indexName, dateFrom, dateTo);
    }

}
