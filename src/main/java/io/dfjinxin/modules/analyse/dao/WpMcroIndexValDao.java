package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
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

//    List<Map<String,Object>> selectdistinctIndexName(@Param("areaName") String areaName,
//                                                     @Param("indexId")Integer indexId);

//    @Select("select t1.*\n" +
//            "from wp_macro_index_info t1\n" +
//            "where t1.index_flag = 0\n" +
//            "group by index_type")
//    List<WpMcroIndexInfoEntity> selectByType();
}
