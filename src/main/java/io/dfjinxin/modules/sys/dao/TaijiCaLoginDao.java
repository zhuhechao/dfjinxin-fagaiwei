package io.dfjinxin.modules.sys.dao;

import io.dfjinxin.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface TaijiCaLoginDao {

    @Insert("insert into sys_user (username,salt,tenant_id) values (#{username},#{salt},#{tenantId})")
    void saveUser(@Param("username") String username,@Param("salt") String salt,@Param("tenantId") Long tenantId);

    @Delete("delete from sys_user where username = #{userid}")
    void removeOldUser(String userid);
}
