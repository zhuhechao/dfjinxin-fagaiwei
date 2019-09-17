package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Repository
@Mapper
public interface SysDepDao extends BaseMapper<SysDepEntity> {
    /**
     * 新增部门信息
     */
    void addDeps(ArrayList<SysDepEntity> list);
}
