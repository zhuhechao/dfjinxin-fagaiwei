package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by GaoPh on 2019/9/5.
 */
@Repository
@Mapper
public interface AuthorizeInfoDao extends BaseMapper<SysUserEntity> {
  @Select("select pui.user_id from pss_user_info pui where user_name = #{userName}")
  String getUserInfo(@Param("userName") String userName);
}
