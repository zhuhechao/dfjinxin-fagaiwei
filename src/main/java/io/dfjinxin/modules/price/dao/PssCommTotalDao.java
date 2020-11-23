package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-23 15:38:58
 */
@Mapper
@Repository
public interface PssCommTotalDao extends BaseMapper<PssCommTotalEntity> {

    List<PssCommTotalEntity> selectSubCommByLevelCode2(@Param("parentCode") Integer parentCode, @Param("param") PssCommTotalDto dto);
    int selectSubCommCountByLevelCode2(@Param("parentCode") Integer parentCode, @Param("param") PssCommTotalDto dto);

    List<PssCommTotalEntity> findCommByLevelCode2(@Param("parentCode") Integer parentCode, @Param("param") PssCommTotalDto dto);
    int findCommCountByLevelCode2(@Param("parentCode") Integer parentCode, @Param("param") PssCommTotalDto dto);

    PssCommTotalEntity getType1CommBySubCommId(@Param("commId")Integer commId);

    List<PssCommTotalEntity> getDistinctSameParentCode(@Param("commIds") Set<Integer> commIds);

    @Select("SELECT COUNT(1) sum,m1.comm_id,m1.comm_name FROM (\n" +
            "SELECT t1.comm_id,t1.comm_name,t1.parent_code FROM pss_comm_total t1\n" +
            "WHERE t1.parent_code = 0) m1\n" +
            "LEFT JOIN (\n" +
            "SELECT t2.level_code,t2.comm_id,t2.comm_name,t2.parent_code FROM pss_comm_total t2\n" +
            "WHERE t2.level_code = 1) m2\n" +
            "ON m1.comm_id = m2.parent_code\n" +
            "LEFT JOIN (\n" +
            "SELECT t3.level_code,t3.parent_code,t3.comm_name FROM pss_comm_total t3\n" +
            "WHERE t3.level_code = 2) m3\n" +
            "ON m2.comm_id = m3.parent_code\n" +
            "GROUP BY m1.comm_id")
    List<Map<String, Object>> getShopCountBycode();
}
