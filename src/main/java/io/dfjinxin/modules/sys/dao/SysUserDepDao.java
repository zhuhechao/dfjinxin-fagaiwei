package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysUserDepEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by GaoPh on 2019/9/4.
 */
@Repository
@Mapper
public interface SysUserDepDao extends BaseMapper<SysUserDepEntity> {

    /**
     * 删除用户和部门关系
     */
    void deleteAll(String userId);

}
