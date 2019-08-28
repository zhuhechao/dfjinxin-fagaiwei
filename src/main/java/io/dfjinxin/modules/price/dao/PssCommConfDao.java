package io.dfjinxin.modules.price.dao;

import io.dfjinxin.modules.price.entity.PssCommConfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    void saveCommConf(@Param("commId") Integer commId, @Param("ewarnIds") List<Integer> ewarnIds);
}
