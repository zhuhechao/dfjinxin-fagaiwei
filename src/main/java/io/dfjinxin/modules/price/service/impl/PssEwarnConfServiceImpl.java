package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssEwarnConfDao;
import io.dfjinxin.modules.price.dto.PageListDto;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import io.dfjinxin.modules.price.service.PssEwarnConfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssEwarnConfEntity> page = this.page(
                new Query<PssEwarnConfEntity>().getPage(params),
                new QueryWrapper<PssEwarnConfEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageList(PageListDto pageListDto) {

        int totalCount = pssEwarnConfDao.queryPageListCount(pageListDto);
        List<PssEwarnConfEntity> returnList = pssEwarnConfDao.queryPageList(pageListDto);
        return new PageUtils(returnList, totalCount, pageListDto.getPageSize(), pageListDto.getPageIndex());
    }

    @Override
    public PssEwarnConfEntity queryEntityByEwarnId(String ewarnId) {

        if (StringUtils.isBlank(ewarnId)) {
            return null;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ewarn_id", ewarnId);
//        0：删除  1：正常
        queryWrapper.eq("del_flag", "1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer queryLastEwarnId() {
        List<PssEwarnConfEntity> ewarnIdList = pssEwarnConfDao.queryLastEwarnId();
        List<Integer> idList = new ArrayList<>();
        for (PssEwarnConfEntity entity : ewarnIdList) {
            String[] strArrs = entity.getEwarnId().split("_");
            idList.add(Integer.valueOf(strArrs[1]));
        }
        return Collections.max(idList);
    }

}
