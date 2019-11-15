package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.entity.PssCommConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 19:33:54
 */
@Mapper
@Repository
public interface PssCommConfDao extends BaseMapper<PssCommConfEntity> {

    void saveCommConf(@Param("commId") Integer commId,
                      @Param("indexId") Integer indexId,
                      @Param("ewarnIds") List<Integer> ewarnIds);


    @Select("select conf.*, ec.ewarn_type_id, tot.comm_name\n" +
            "from pss_comm_conf conf\n" +
            "         left join pss_comm_total tot on tot.comm_id = conf.comm_id\n" +
            "         left join pss_ewarn_conf ec on ec.ewarn_id = conf.ewarn_id\n" +
            "where conf.comm_id in (select t.comm_id from pss_comm_total t where t.data_flag = 0 " +
            "and t.parent_code = #{commId} )\n" +
            "  and conf.del_flag = 0\n" +
            "  and ec.del_flag = 0\n" +
            "  and ec.ewarn_type_id = #{ewarnTypeId}")
    List<PssCommConfEntity> queryByewarnTypeId(@Param("commId")Integer commId,
                                                @Param("ewarnTypeId")Integer ewarnTypeId);
}
