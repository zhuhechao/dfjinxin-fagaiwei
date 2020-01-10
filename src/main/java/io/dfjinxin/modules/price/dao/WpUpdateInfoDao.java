package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.entity.WpUpdateInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2020-01-10 15:52:24
 */
@Mapper
public interface WpUpdateInfoDao extends BaseMapper<WpUpdateInfoEntity> {

    @Select("select count\n" +
            "from wp_update_info\n" +
            "where date(update_time) = '${dateStr}'\n" +
            "order by update_time desc\n" +
            "limit 0,1")
    Long getEverydayInfoTotal(@Param("dateStr") String  dateStr);
}
