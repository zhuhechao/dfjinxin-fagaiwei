package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.sys.dao.SysDepDao;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysDepService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Service("sysDepService")
@Transactional(rollbackFor = Exception.class)
public class SysDepServiceImpl extends ServiceImpl<SysDepDao, SysDepEntity> implements SysDepService {
  @Autowired
  private SysDepDao sysDepDao;

  @Override
    public void addDeps(List<SysDepEntity> depEntities) {
          sysDepDao.addDeps(depEntities);
    }

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    long no = params.containsKey("pageIndex") ? Long.valueOf(params.get("pageIndex").toString()) : 1;
    long limit = params.containsKey("pageSize") ? Long.valueOf(params.get("pageSize").toString()) : 10;
    IPage<SysDepEntity> page = baseMapper.queryDep(new Page<>(no, limit), params);
    List<SysDepEntity> list = page.getRecords();
    for(SysDepEntity map:list){
      Integer st= map.getDepState();
      if(st == 1){
        map.setStatus(true);
      }else {
        map.setStatus(false);
      }
    }
    return new PageUtils(page);
  }

  @Override
  public List<Map<String, Object>> serDepInfo() {
    return sysDepDao.searchDepInfo();
  }

  @Override
  public SysDepEntity getDepId(String depId) {
      Map<String,Object> map = new HashMap<>();
      map.put("depId",depId);
    return baseMapper.queryDep(map);
  }
}
