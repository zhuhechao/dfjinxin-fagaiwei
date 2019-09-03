package io.dfjinxin.modules.price.service.impl;

import io.dfjinxin.modules.price.dto.PssBigdataFormInfoDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.PssBigdataFormInfoDao;
import io.dfjinxin.modules.price.entity.PssBigdataFormInfoEntity;
import io.dfjinxin.modules.price.service.PssBigdataFormInfoService;


@Service("pssBigdataFormInfoService")
public class PssBigdataFormInfoServiceImpl extends ServiceImpl<PssBigdataFormInfoDao, PssBigdataFormInfoEntity> implements PssBigdataFormInfoService {

    @Override
    public List<PssBigdataFormInfoDto> listAll() {
        List<PssBigdataFormInfoEntity> list = super.list();
        List<PssBigdataFormInfoDto> dtos = new ArrayList();

        for(PssBigdataFormInfoEntity entity:list) {
            dtos.add(PssBigdataFormInfoEntity.toData(entity));
        }
        return dtos;
    }
}
