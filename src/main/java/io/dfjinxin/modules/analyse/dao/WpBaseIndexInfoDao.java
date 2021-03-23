package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.price.dto.DataSetIndexInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:20
 */
@Mapper
@Repository
public interface WpBaseIndexInfoDao extends BaseMapper<WpBaseIndexInfoEntity> {

    @Select("select info.index_id, info.comm_id, info.frequence, info.area_name,info.index_name,tot.comm_name\n" +
            "from wp_base_index_info info\n" +
            "         left join pss_comm_total tot on info.comm_id = tot.comm_id\n" +
            "where tot.data_flag = 0\n" +
            "  and index_flag = 0\n" +
            "  and info.index_id = #{indexId}")
    DataSetIndexInfoDto getIndexInfoByIndexId(@Param("indexId") Object indexId);


   @Select("select  comm_id,index_type,index_name,index_used,unit,area_name,frequence,source_name,index_flag , count( DISTINCT area_name)  from wp_base_index_info wbii where wbii.comm_id = #{indexId} and wbii.index_flag = 0 and wbii.index_type = #{indexType} and wbii.index_used  #{indexUsed} and wbii.index_name = #{indexName}   GROUP BY wbii.area_name ")
    List<WpBaseIndexInfoEntity> selIndexName(@Param("indexId")Integer indexId,@Param("indexType")String indexType,@Param("indexUsed")String indexUsed,@Param("indexName")String indexName);


    @Select("select  comm_id,index_type,index_name,index_used,unit,area_name,frequence,source_name,index_flag , count( DISTINCT index_name)  from wp_base_index_info wbii where wbii.comm_id = #{indexId} and wbii.index_flag = 0 and wbii.index_name = #{indexName} and wbii.index_used = #{indexUsed} and wbii.area_name = #{areaName}  GROUP BY wbii.index_name ")
    List<WpBaseIndexInfoEntity> selAreaName(@Param("indexId")Integer indexId,@Param("indexType")String indexType,@Param("indexUsed")String indexUsed,@Param("areaName")String areaName);

    @Select("select  comm_id,index_type,index_name,index_used,unit,area_name,frequence,source_name,index_flag , count( DISTINCT index_name)  from wp_base_index_info wbii where wbii.comm_id = #{indexId} and wbii.index_flag = 0 and wbii.index_type = #{indexType} and wbii.index_used = #{indexUsed} and wbii.area_name = #{areaName}  GROUP BY wbii.index_name ")
    List<WpBaseIndexInfoEntity> selIndexUsed(@Param("indexId")Integer indexId,@Param("indexType")String indexType,@Param("indexUsed")String indexUsed,@Param("areaName")String areaName);


}
