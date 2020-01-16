package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.entity.SysDepEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
public interface SysDepService extends IService<SysDepEntity> {
    /**
     * 批量新增部门信息
     */
    void addDeps(List<SysDepEntity> depEntities);

    /**
     * 查询部门信息
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     *用户管理部门信息下拉框
     */
    List<Map<String,Object>> serDepInfo();

    /**
     * 获取指定的部门信息
     */
    SysDepEntity getDepId(String depId);

}
