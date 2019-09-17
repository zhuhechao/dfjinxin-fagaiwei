package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@Mapper
@Repository
public interface WpCommIndexValDao extends BaseMapper<WpCommIndexValEntity> {
    List<Map<String, Object>> queryIndexTypeByCommId(@Param("commId") Integer commId);

    List<Map<String, Object>> queryIndexTypeByCondition(@Param("condition") Map<String, Object> condition);

    List<Map<String, Object>> queryIndexTypePrice(@Param("condition") Map<String, Object> condition);

    List<WpCommIndexValEntity> selectListBystatAreaId(@Param("commId")int commId,
                                                      @Param("indexType")String indexType,
                                                      @Param("indexId")Integer indexId);
}
