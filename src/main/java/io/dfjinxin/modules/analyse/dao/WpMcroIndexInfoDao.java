package io.dfjinxin.modules.analyse.dao;

import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.dto.DataSetIndexInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@Mapper
@Repository
public interface WpMcroIndexInfoDao extends BaseMapper<WpMcroIndexInfoEntity> {

    @Select("select t1.*\n" +
            "from wp_macro_index_info t1\n" +
            "where t1.index_flag = 0\n" +
            "group by index_type")
    List<WpMcroIndexInfoEntity> selectByType();

    @Select("select info.index_id, info.frequence, info.index_name, info.area_name\n" +
            "from wp_macro_index_info info\n" +
            "where index_flag = 0\n" +
            "and index_id=#{indexId}")
    DataSetIndexInfoDto getIndexInfoByIndexId(@Param("indexId") Object indexId);
}
