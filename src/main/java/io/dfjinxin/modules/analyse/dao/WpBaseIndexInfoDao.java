package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.price.dto.DataSetIndexInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
}
