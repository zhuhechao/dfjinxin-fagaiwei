package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.sys.dao.SysUserDao;
import io.dfjinxin.modules.sys.dao.SysUserDepDao;
import io.dfjinxin.modules.sys.entity.SysUserDepEntity;
import io.dfjinxin.modules.sys.service.SysUserDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Service("sysUserDepService")
@Transactional(rollbackFor = Exception.class)
public class SysUserDepImpl extends ServiceImpl<SysUserDepDao,SysUserDepEntity> implements SysUserDepService {
    @Autowired
    private SysUserDepDao sysUserDepDao;

    @Override
    public void deleteAll(String userId) {
        sysUserDepDao.deleteAll(userId);
    }

    @Override
    public List<SysUserDepEntity> queryUserDep(Map<String, Object> params) {
        return null;
    }

    @Override
    public void saveDep(ArrayList<SysUserDepEntity> list) {
     this.saveBatch(list);
    }
}
