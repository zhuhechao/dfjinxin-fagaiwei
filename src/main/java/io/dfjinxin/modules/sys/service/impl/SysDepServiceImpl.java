package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.sys.dao.SysDepDao;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.service.SysDepService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Service("sysDepService")
public class SysDepServiceImpl extends ServiceImpl<SysDepDao, SysDepEntity> implements SysDepService {
  @Autowired
  private SysDepDao sysDepDao;

    @Override
    public void addDeps(ArrayList<SysDepEntity> depEntities) {
          sysDepDao.addDeps(depEntities);
    }

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<SysDepEntity> page = this.page(
            new Query<SysDepEntity>().getPage(params),
            new QueryWrapper<SysDepEntity>()
    );
    return new PageUtils(page);
  }
}
