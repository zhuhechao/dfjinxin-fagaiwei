/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.modules.sys.dao.SysMenuDao;
import io.dfjinxin.modules.sys.dao.SysUserDao;
import io.dfjinxin.modules.sys.dao.SysUserTokenDao;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(String userId) {

        //查询用户权限
        List<String>  permsList = sysUserDao.queryAllPerms(userId);
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(String userId) {

        return sysUserDao.queryByUserId(userId);
    }

    @Override
    public SysUserEntity queryUserByName(SysUserEntity entity) {
        return sysUserDao.queryByUserName(entity);
    }

    @Override
    public void removeUserDBCache(String userName) {
        sysUserDao.deleteByUserName(userName);
    }

    @Override
    public List<SysUserTokenEntity> queryAllTokenUser() {
        return baseMapper.queryAllUserToken() ;
    }
}
