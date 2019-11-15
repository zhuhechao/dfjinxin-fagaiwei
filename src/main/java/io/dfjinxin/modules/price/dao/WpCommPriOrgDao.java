package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.entity.WpCommPriOrgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-11-14 17:23:11
 */
@Mapper
@Repository
public interface WpCommPriOrgDao extends BaseMapper<WpCommPriOrgEntity> {

    @Select("select *\n" +
            "from wp_comm_pri_org where index_id=#{indexId}\n" +
            "and data_time between #{startDate} and #{endDate}\n" +
            "order by data_time desc ")
    List<WpCommPriDto> queryIndexByDate(@Param("indexId") Integer indexId,
                                        @Param("startDate")String startDate,
                                        @Param("endDate")String endDate);
}
