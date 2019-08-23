package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.dto.PageListDto;
import io.dfjinxin.modules.price.entiry.PssEwarnConfEntity;
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

    Integer queryPageListCount(@Param("param") PageListDto pageListDto);

    List<PssEwarnConfEntity> queryPageList(@Param("param") PageListDto pageListDto);
}
