package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.price.dao.PssBigdataFormInfoDao;
import io.dfjinxin.modules.price.dto.PssBigdataFormInfoDto;
import io.dfjinxin.modules.price.entity.PssBigdataFormInfoEntity;
import io.dfjinxin.modules.price.service.PssBigdataFormInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
