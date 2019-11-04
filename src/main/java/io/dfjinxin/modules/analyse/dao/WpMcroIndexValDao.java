package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @date 2019-09-09 11:23:36
 */
@Mapper
@Repository
public interface WpMcroIndexValDao extends BaseMapper<WpMcroIndexValEntity> {

    List<Map<String,Object>> selectdistinctIndexName(@Param("areaName") String areaName,
                                                     @Param("indexId")Integer indexId);

    @Select("select t1.*,t2.code_name as codeName from wp_macro_index_info t1 left join wp_ascii_info t2\n" +
            "on t1.index_type = t2.code_id\n" +
            "where index_flag=0 group by index_type")
    List<WpMcroIndexInfoEntity> selectByType();
}
