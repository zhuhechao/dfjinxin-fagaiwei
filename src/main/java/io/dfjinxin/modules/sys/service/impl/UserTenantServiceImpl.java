package io.dfjinxin.modules.sys.service.impl;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.dao.UserTenantDao;
import io.dfjinxin.modules.sys.entity.UserTenantEntity;
import io.dfjinxin.modules.sys.service.UserTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.Query;

@Service("userTenantService")
public class UserTenantServiceImpl extends ServiceImpl<UserTenantDao, UserTenantEntity> implements UserTenantService {

    @Autowired
    private UserTenantDao userTenantDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTenantEntity> page = this.page(
                new Query<UserTenantEntity>().getPage(params),
                new QueryWrapper<UserTenantEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserTenantEntity> queryAll(){
        return userTenantDao.queryAll();
    }
    
}