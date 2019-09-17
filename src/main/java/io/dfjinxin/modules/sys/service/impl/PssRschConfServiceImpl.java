package io.dfjinxin.modules.sys.service.impl;

import io.dfjinxin.common.utils.QuartzUtil;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.sys.dao.PssRschConfDao;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.dfjinxin.modules.sys.service.PssRschConfService;


@Service("pssRschConfService")
public class PssRschConfServiceImpl extends ServiceImpl<PssRschConfDao, PssRschConfEntity> implements PssRschConfService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssRschConfEntity> page = this.page(
                new Query<PssRschConfEntity>().getPage(params),
                new QueryWrapper<PssRschConfEntity>().ne("del_flag","1").orderByDesc("create_time")
        );

        return new PageUtils(page);
    }



}