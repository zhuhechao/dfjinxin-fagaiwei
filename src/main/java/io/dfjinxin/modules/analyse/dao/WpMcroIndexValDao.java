package io.dfjinxin.modules.analyse.dao;

import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @date 2019-09-09 11:23:36
 */
@Mapper
@Repository
public interface WpMcroIndexValDao extends BaseMapper<WpMcroIndexValEntity> {

    List<Map<String,Object>> selectdistinctIndexName(@Param("areaName") String areaName,
                                                     @Param("indexId")Integer indexId);
}