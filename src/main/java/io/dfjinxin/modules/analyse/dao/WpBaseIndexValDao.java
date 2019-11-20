package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@Mapper
@Repository
public interface WpBaseIndexValDao extends BaseMapper<WpBaseIndexValEntity> {
    List<Map<String, Object>> queryIndexTypeByCommId(@Param("commId") Integer commId);

    List<Map<String, Object>> queryIndexTypeByCondition(@Param("condition") Map<String, Object> condition);

    List<Map<String, Object>> queryIndexTypePrice(@Param("condition") Map<String, Object> condition);

    /*List<WpBaseIndexValEntity> selectListBystatAreaId(@Param("commId")int commId,
                                                      @Param("indexType")String indexType,
                                                      @Param("indexId")Integer indexId);*/

    List<WpBaseIndexValEntity> queryByIndexType(@Param("commId") Integer commId, @Param("indexType") String indexType);

    List<WpBaseIndexValEntity> queryMapValByIndexType(@Param("commId") Integer commId);

    List<WpBaseIndexValEntity> queryNoPriceByIndexType(@Param("commId") Integer commId, @Param("indexType") String indexType);

    IPage<PssPriceReltEntity> queryPageByDate(Page page, @Param("param") Map map);

    @Select("SELECT val.*,tol.comm_name\n" +
            "FROM wp_base_index_val val\n" +
            "left join pss_comm_total tol on val.comm_id=tol.comm_id\n" +
            "WHERE val.comm_id IN (select pss_comm_total.comm_id\n" +
            "                  FROM pss_comm_total\n" +
            "                  WHERE data_flag = 0\n" +
            "                    and parent_code = #{commId})\n" +
            "  AND val.date = #{lastDayStr}\n" +
            "GROUP BY val.comm_id")
    List<WpBaseIndexValEntity> queryLastDayPriceByCommId(
            @Param("commId") Integer commId,
            @Param("lastDayStr") String lastDayStr);
}