package io.dfjinxin.modules.price.service.impl;

import io.dfjinxin.modules.price.service.WpAsciiInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.price.dao.WpAsciiInfoDao;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;


@Service("wpAsciiInfoService")
public class WpAsciiInfoServiceImpl extends ServiceImpl<WpAsciiInfoDao, WpAsciiInfoEntity> implements WpAsciiInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpAsciiInfoEntity> page = this.page(
                new Query<WpAsciiInfoEntity>().getPage(params),
                new QueryWrapper<WpAsciiInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpAsciiInfoEntity> getInfoByCodeId(String params) {

        if (StringUtils.isBlank(params)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder("select code_id from wp_ascii_info where code_id = '");
        sql.append(params);
        sql.append("'");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code_status", 0);
        queryWrapper.inSql("p_code_val", sql.toString());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<WpAsciiInfoEntity> getInfoAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code_status", 0);
        queryWrapper.eq("p_code_val", 0);
        return baseMapper.selectList(queryWrapper);
    }

}
