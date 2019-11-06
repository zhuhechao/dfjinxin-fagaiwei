package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
}
