package io.dfjinxin.modules.price.dao;

import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
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
 * @date 2019-08-26 17:21:32
 */
@Mapper
@Repository
public interface PssPriceEwarnDao extends BaseMapper<PssPriceEwarnEntity> {

    List<PssPriceEwarnEntity> queryList(@Param("commId") Integer commId);
}
