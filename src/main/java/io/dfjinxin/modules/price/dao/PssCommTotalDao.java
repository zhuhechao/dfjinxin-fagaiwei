package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-23 15:38:58
 */
@Mapper
@Repository
public interface PssCommTotalDao extends BaseMapper<PssCommTotalEntity> {

    int queryPageListCount(@Param("param") PssCommTotalDto pssCommTotalDto);

    List<PssEwarnConfEntity> queryPageList(@Param("param") PssCommTotalDto pssCommTotalDto);
}
