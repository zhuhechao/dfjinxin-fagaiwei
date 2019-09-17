package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.entity.SysUserDepEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/4.
 */
public interface SysUserDepService extends IService<SysUserDepEntity> {
    /**
     * 删除用户的部门关系
     * @param userId
     */
    void deleteAll(String userId);

    /**
     * 查询部门信息
     */
    List<SysUserDepEntity> queryUserDep(Map<String, Object> params);

    /**
     * 保存部门信息
     */
    void saveDep(ArrayList<SysUserDepEntity> sysUserDepEntity);

}
