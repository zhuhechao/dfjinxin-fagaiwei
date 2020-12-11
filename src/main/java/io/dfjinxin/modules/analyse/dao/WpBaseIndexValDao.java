package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
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

    List<WpBaseIndexValEntity> queryByIndexType(@Param("commId") Integer commId, @Param("indexType") String indexType, @Param("date") String date);

    List<WpBaseIndexValEntity> queryMapValByIndexType(@Param("commId") Integer commId,@Param("date") String date);

    List<WpBaseIndexValEntity> queryNoPriceByIndexType(@Param("commId") Integer commId, @Param("indexType") String indexType);

    IPage<PssPriceReltEntity> queryPageByDate(Page page, @Param("param") Map map);

    @Select("select val.*,total.comm_name from wp_base_index_val val \n" +
            "left join pss_comm_total total on val.comm_id = total.comm_id \n" +
            "where val.comm_id in (select comm_id from pss_comm_total \n" +
            "where data_flag=0 and parent_code = #{param.commId}) \n" +
            "and val.index_type = #{param.indexType} AND val.date >= #{param.startDate} AND val.date <= #{param.endDate} ")
    List<Map<String, Object>> downloadByDate(@Param("param") Map map);

    @Select("SELECT val.*,tol.comm_name\n" +
            "FROM wp_base_index_val val\n" +
            "left join pss_comm_total tol on val.comm_id=tol.comm_id\n" +
            "WHERE val.comm_id IN (select pss_comm_total.comm_id\n" +
            "                  FROM pss_comm_total\n" +
            "                  WHERE data_flag = 0\n" +
            "                    and parent_code = #{commId})\n" +
            "  AND val.date <= #{lastDayStr}\n" +
            "GROUP BY val.comm_id")
    List<WpBaseIndexValEntity> queryLastDayPriceByCommId(
            @Param("commId") Integer commId,
            @Param("lastDayStr") String lastDayStr);

    @Select("SELECT tol.comm_id,tol.comm_name,tol.parent_code,tol.level_code,tol.data_flag,\n" +
            "\t\t\t\t\t\t\t\ttol.create_time\n" +
            "                        FROM pss_comm_total tol\n" +
            "            left join wp_base_index_val val on val.comm_id=tol.comm_id\n" +
            "            WHERE val.comm_id IN (select pss_comm_total.comm_id\n" +
            "                              FROM pss_comm_total\n" +
            "                              WHERE data_flag = 0\n" +
            "                                and parent_code = #{commId})\n" +
            "            and val.index_type=#{indexType} \n" +
            "            group by tol.comm_id")
    List<PssCommTotalEntity> queryCommListByCommId(@Param("commId") Integer commId,
                                                   @Param("indexType") String indexType);

    @Select("SELECT val.*, tol.comm_name\n" +
            "from wp_base_index_val val\n" +
            "         left join pss_comm_total tol\n" +
            "                   on val.comm_id = tol.comm_id\n" +
            "WHERE val.comm_id = #{commId}\n" +
            "  and val.index_type = #{indexType}\n" +
            "  and tol.data_flag = 0\n" +
            "  and val.date = #{lastDayStr}\n" +
            "  and (area_name like '%省' or area_name like '%自治区' or area_name like '%市')\n")
    List<WpBaseIndexValEntity> getProvinceMapByCommId(
            @Param("commId") Integer commId,
            @Param("lastDayStr") String lastDayStr,
            @Param("indexType") String indexType);

    @Select("SELECT COUNT(1) count FROM wp_base_index_val t")
    List<Map<String, Object>> getDataCount();

    @Select("SELECT n.comm_name,t.comm_id FROM wp_base_index_val t\n" +
            "LEFT JOIN pss_comm_total n ON t.comm_id = n.comm_id\n" +
            "WHERE n.parent_code = #{p.commId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "AND t.index_type =  '价格'\n" +
            "AND t.frequence = '日'\n" +
//            "AND t.area_name in ('全国','中国')\n" +
            "GROUP BY t.comm_id")
    List<Map<String, Object>> getIndexThendCommIds(@Param("p") Map<String, Object> params);

    @Select("SELECT t.index_id,t.index_name FROM wp_base_index_val t\n" +
            "WHERE t.comm_id = #{p.indexId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "AND t.index_type =  '价格'\n" +
            "AND t.frequence = '日'\n" +
//            "AND t.area_name in ('全国','中国')\n" +
            "GROUP BY t.index_id")
    List<Map<String, Object>> getIndexThendIndexs(@Param("p") Map<String, Object> params);

    @Select("SELECT t.index_id,t.date,t.index_name,t.value,t.unit FROM wp_base_index_val t\n" +
            "WHERE t.index_id = #{p.indexId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "AND t.index_type =  '价格'\n" +
            "AND t.frequence = '日'\n" +
            "AND t.value >0\n" +
//            "AND t.area_name in ('全国','中国')\n" +
            "GROUP BY t.date\n" +
            "order BY t.date")
    List<Map<String, Object>> getIndexThend(@Param("p") Map<String, Object> params);


    @Select("SELECT t.comm_id,pss.comm_name FROM wp_base_index_val t\n" +
            "            LEFT JOIN pss_comm_total pss ON pss.comm_id = t.comm_id\n" +
            "WHERE  t.index_type =  #{p.indexType}\n" +
            "AND pss.parent_code = #{p.commId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.comm_id\n" +
            "order BY t.comm_id")
    List<Map<String, Object>> getJiaGeCommList(@Param("p") Map<String, Object> params);

    @Select("SELECT  t.index_id,t.index_name FROM wp_base_index_val t\n" +
            "WHERE  t.index_type =  #{p.indexType}\n" +
            "AND t.comm_id = #{p.id}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.comm_id\n" +
            "order BY t.comm_id")
    List<Map<String, Object>> getJiaGeIndexList(@Param("p") Map<String, Object> params);

    @Select("SELECT  t.index_id,t.index_name,t.date,t.value,t.unit FROM wp_base_index_val t\n" +
            "WHERE t.value>0\n" +
            "AND t.index_type =  #{p.indexType}\n" +
            "AND t.index_id = #{p.indexId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.date\n" +
            "order BY t.date")
    List<Map<String, Object>> getJiaGeIndexData(@Param("p") Map<String, Object> params);

    @Select("SELECT n.comm_name,t.index_id,t.date,t.index_name,t.comm_id,t.source_name,t.value,t.unit FROM wp_base_index_val t\n" +
            "LEFT JOIN pss_comm_total n ON t.comm_id = n.comm_id\n" +
            "WHERE n.parent_code = #{p.commId}\n" +
            "AND t.date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "AND t.index_type =  #{p.indexType}\n" +
            "AND t.frequence = '日'\n" +
            "AND t.value >= 0\n" +
            "GROUP BY t.index_id,t.date\n" +
            "order BY t.date")
    List<Map<String, Object>> getIndexThendAll(@Param("p") Map<String, Object> params);

    @Select("SELECT t.* FROM wp_base_index_val t\n" +
            "            LEFT JOIN pss_comm_total m ON m.comm_id = t.comm_id\n" +
            "            WHERE t.index_type = '价格'\n" +
            "            AND m.parent_code = #{p.commId}\n" +
            "            AND t.area_name IN (SELECT p.area_name FROM wp_area_info p WHERE p.area_id BETWEEN 1 AND 32)\n" +
            "       AND t.date in (SELECT MAX(t1.date) FROM wp_base_index_val t1\n" +
            "            LEFT JOIN pss_comm_total m1 ON m1.comm_id = t1.comm_id\n" +
            "            WHERE t1.index_type = '价格'\n" +
            "            AND m1.parent_code = #{p.commId}\n" +
            "            AND t1.area_name IN (SELECT p.area_name FROM wp_area_info p WHERE p.area_id BETWEEN 1 AND 32)\n" +
            "\t\t\t\t\t\t)\n" +
            "            GROUP BY t.area_name\n" +
            "ORDER BY t.date desc")
    List<Map<String, Object>> getProvince(@Param("p") Map<String, Object> params);

    @Select("SELECT t.* FROM wp_base_index_val t\n" +
            "LEFT JOIN pss_comm_total n ON n.comm_id= t.comm_id\n" +
            "WHERE t.index_type = '价格'\n" +
            "AND t.area_name = #{p.province}\n" +
            "AND n.parent_code = #{p.commId}\n" +
            "AND t.date = #{p.itemDate}\n" +
            "GROUP BY t.comm_id")
    List<Map<String, Object>> getProvinceCommList(@Param("p") Map<String, Object> params);

    @Select("SELECT t.comm_id,n.comm_name,t.date FROM wp_base_index_val t\n" +
            "LEFT JOIN pss_comm_total n ON t.comm_id = n.comm_id\n" +
            "WHERE  n.parent_code = #{p.commId}\n" +
            " AND t.area_name IN (SELECT p.area_name FROM wp_area_info p WHERE p.area_id BETWEEN 1 AND 32)\n" +
            "GROUP BY t.comm_id")
    List<Map<String, Object>> getCommList(@Param("p") Map<String, Object> params);

    @Select("SELECT t.*,n.comm_name FROM wp_base_index_val t\n" +
            "LEFT JOIN pss_comm_total n ON t.comm_id = n.comm_id\n" +
            "WHERE  t.comm_id = #{p.commId}\n" +
            "AND t.date = #{p.commDate}\n" +
            "AND t.index_type  = '价格'\n" +
            "AND t.area_name IN (SELECT p.area_name FROM wp_area_info p WHERE p.area_id BETWEEN 1 AND 32)\n" +
            "GROUP  BY t.area_name")
    List<Map<String, Object>> getCommProvinceList(@Param("p") Map<String, Object> params);



    @Select("SELECT t.index_id FROM wp_base_index_val t \n" +
            "            WHERE  t.index_type =#{p.indexType}\n" +
            "            AND t.comm_id = #{p.indexId}\n" +
            "            GROUP BY t.index_id\n" +
            "            order BY t.index_id desc")
    List<Map<String, Object>> getOtherIndex(@Param("p") Map<String, Object> params);

    @Select("SELECT concat(t.index_name, '--', t.area_name) index_name,t.index_id,t.`value`,t.date,t.unit FROM wp_base_index_val t\n" +
            "            WHERE  t.index_type =#{p.indexType}\n" +
            "            AND t.index_id = #{p.indexId}\n" +
            "            GROUP BY t.date\n" +
            "            order BY t.date desc\n" +
            "            LIMIT 1000")
    List<Map<String, Object>> getRiOrYueOrNianList(@Param("p") Map<String, Object> params);

    @Select("SELECT t.comm_id,pss.comm_name FROM wp_base_index_val t\n" +
            "            LEFT JOIN pss_comm_total pss ON pss.comm_id = t.comm_id\n" +
            "WHERE  t.index_type =  #{p.indexType}\n" +
            "AND pss.parent_code = #{p.commId}\n" +
            "GROUP BY t.comm_id\n" +
            "order BY t.comm_id " )
    List<Map<String, Object>> getRiOrYueOrNianCommId(@Param("p") Map<String, Object> params);

    @Select("SELECT t.index_id,concat(t.index_name, '--', t.area_name) index_name,t.frequence FROM wp_base_index_val t     \n" +
            "WHERE  t.index_type =  #{p.indexType}\n" +
            "AND t.comm_id  = #{p.commId}\n" +
            "GROUP BY t.index_id\n" +
            "order BY t.index_id " )
    List<Map<String, Object>> getRiOrYueOrNianIndexId(@Param("p") Map<String, Object> params);

    @Select("SELECT t.index_id,concat(t.index_name, '--', t.area_name) index_name,t.`value`,t.date,t.unit FROM wp_base_index_val t \n" +
            "WHERE  t.index_type =  #{p.indexType}\n" +
            "AND t.index_id  = #{p.indexId}\n" +
            "AND t.value>0\n" +
            "GROUP BY t.date\n" +
            "order BY t.date \n" +
            "LIMIT 1000" )
    List<Map<String, Object>> getRiOrYueOrNianData(@Param("p") Map<String, Object> params);

    @Select("SELECT t.comm_id commId,p.comm_name commName,info.index_name indexName,info.source_name sourceName,info.frequence,\n" +
            "           DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') date,t.pric_type_id indexId,\n" +
            "            t.pri_range pro,t.pri_value value,t.stat_area_code areaName,t.unit" +
            "  FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total p ON t.comm_id = p.comm_id \n" +
            "LEFT JOIN wp_base_index_info info ON  t.pric_type_id =  info.index_id \n" +
            "WHERE t.pri_value >0\n" +
            "AND p.parent_code = #{p.commId}\n" +
            "AND t.ewarn_date >= #{p.startDate}\n" +
            "AND t.ewarn_date <= #{p.endDate}\n" +
            "GROUP BY t.ewarn_date\n" +
            "ORDER BY t.ewarn_date DESC\n" +
            "LIMIT 10")
    List<Map<String, Object>> getPage( @Param("p") Map map);
}
