package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.UserTenantEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户租户信息表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-12 17:06:08
 */
@Repository
@Mapper
public interface UserTenantDao extends BaseMapper<UserTenantEntity> {

    List<UserTenantEntity> queryAll();
}
