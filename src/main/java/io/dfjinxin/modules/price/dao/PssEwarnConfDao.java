package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.common.dto.PssEwarnConfDto;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 价格预警配置表
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-22 14:55:38
 */
@Mapper
@Repository
public interface PssEwarnConfDao extends BaseMapper<PssEwarnConfEntity> {

    Integer queryPageListCount(@Param("param") PssEwarnConfDto pssEwarnConfDto);

    List<PssEwarnConfEntity> queryPageList(@Param("param") PssEwarnConfDto pssEwarnConfDto);

}
