package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Repository
@Mapper
public interface SysDepDao extends BaseMapper<SysDepEntity> {
    /**
     * 获取部门信息--分页
     */
    IPage<SysDepEntity> queryDep(Page page, @Param("m") Map<String, Object> m);

    /**
     * 获取指定的部门信息
     */
    SysDepEntity queryDep( @Param("m") Map<String, Object> m);

    /**
     * 新增部门信息
     */
    void addDeps(List<SysDepEntity> list);

    /**
     * 用户管理部门信息下拉框
     */
    List<Map<String,Object>> searchDepInfo();
}
