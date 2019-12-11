package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.PssEwarnConfDto;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssEwarnConfDao;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;
import io.dfjinxin.modules.price.service.PssEwarnConfService;
import io.dfjinxin.modules.price.service.WpAsciiInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/8/22 15:11
 * @Version: 1.0
 */
@Service("pssEwarnConfService")
public class PssEwarnConfServiceImpl extends ServiceImpl<PssEwarnConfDao, PssEwarnConfEntity> implements PssEwarnConfService {

    @Autowired
    private PssEwarnConfDao pssEwarnConfDao;

    @Autowired
    private WpAsciiInfoService wpAsciiInfoService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssEwarnConfEntity> page = this.page(
                new Query<PssEwarnConfEntity>().getPage(params),
                new QueryWrapper<PssEwarnConfEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageList(PssEwarnConfDto pssEwarnConfDto) {

        int totalCount = pssEwarnConfDao.queryPageListCount(pssEwarnConfDto);
        List<PssEwarnConfEntity> returnList = pssEwarnConfDao.queryPageList(pssEwarnConfDto);
        return new PageUtils(returnList, totalCount, pssEwarnConfDto.getPageSize(), pssEwarnConfDto.getPageIndex());
    }

    @Override
    public PssEwarnConfEntity queryEntityByEwarnId(String ewarnId) {

        if (StringUtils.isBlank(ewarnId)) {
            return null;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ewarn_id", ewarnId);
//        1：删除  0：正常
        queryWrapper.eq("del_flag", "0");
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public List<PssEwarnConfEntity> getWarnTypeAndName(String codeSimple) {

        if (StringUtils.isBlank(codeSimple)) {
            return new ArrayList<>();
        }

        List<WpAsciiInfoEntity> list = wpAsciiInfoService.getInfoByCodeId(codeSimple);
        List resultList = new ArrayList();
        for (WpAsciiInfoEntity entity : list) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("ewarn_type_id", entity.getCodeId());
            queryWrapper.eq("del_flag", 0);
            List<PssEwarnConfEntity> pssEwarnConfEntityList = pssEwarnConfDao.selectList(queryWrapper);
            entity.setEwarnNamelist(pssEwarnConfEntityList);
            resultList.add(entity);
        }
        return resultList;
    }

}
