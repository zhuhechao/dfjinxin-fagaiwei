package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.dto.PwwPriceEwarnDto;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 17:21:32
 */
@Mapper
@Repository
public interface PssPriceEwarnDao extends BaseMapper<PssPriceEwarnEntity> {

    @Select("select date(ppe.ewarn_date) as ewarnDate,count(*) as ewarnCount \n" +
            "from pss_price_ewarn ppe\n" +
            "where ppe.ewarn_level=#{ewarnLevel} \n" +
            "and date(ppe.ewarn_date) between '${startDateStr}' and '${endDateStr}'\n" +
            "group by date(ppe.ewarn_date)")
    List<PwwPriceEwarnDto> getEwarnCountByDate(@Param("ewarnLevel") String ewarnLevel,
                                               @Param("startDateStr") String startDateStr,
                                               @Param("endDateStr") String endDateStr);

    List<PssPriceEwarnEntity> queryEwarnlevel();

    Map<String, Object> selectMaxRange(
            @Param("commId") Integer commId,
            @Param("dateStr") String dateStr);

    List<PssPriceEwarnEntity> queryPriceEwarnByDate(@Param("commId") Integer commId,
                                                    @Param("dateStr") String dateStr);

    List<Map<Integer, Object>> countEwarn(@Param("dateStr") String dateStr);

    @Select("select pl.comm_name,pl.comm_id,pn.ewarn_date, pn.pri_range from pss_price_ewarn pn " +
            "LEFT JOIN (select * from pss_comm_total pl where pl.parent_code=#{params.commId}) pl " +
            "on pn.comm_id=pl.comm_id " +
            "where pn.ewarn_date between STR_TO_DATE(#{params.beginYestarday},'%Y-%m-%d %H:%i:%S') " +
            " and STR_TO_DATE(#{params.endYestarday},'%Y-%m-%d %H:%i:%S') " +
            "and pl.comm_id is not null")
    List<Map<String, Object>> getDayReport(@Param("params") Map<String, Object> params);

    /**
     * @Desc: 根据1类商品查询4类商品 在指定时间内的各预警级别的总数
     * @Param: [commId]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssCommTotalEntity>
     * @Author: z.h.c
     * @Date: 2019/11/21 15:20
     */
    @Select("select case ppe.ewarn_level\n" +
            "           when 71 then 'red'\n" +
            "           when 72 then 'orange'\n" +
            "           when 73 then 'yellow'\n" +
            "           when 74 then 'green'\n" +
            "           end as levelType,\n" +
            "           count(1) as ewarnCount \n" +
            "from pss_comm_total comm left join pss_price_ewarn ppe on comm.comm_id = ppe.comm_id\n" +
            "where data_flag = 0\n" +
            "  and level_code = 3\n" +
            "  and parent_code in (\n" +
            "    select comm_id\n" +
            "    from pss_comm_total\n" +
            "    where data_flag = 0\n" +
            "      and level_code = 2\n" +
            "      and parent_code in (\n" +
            "        select comm_id from pss_comm_total where data_flag = 0 and level_code = 1 and parent_code = #{commId}))\n" +
            "  and date(ppe.ewarn_date) between #{startDateStr} and #{endDateStr}\n" +
            "group by ppe.ewarn_level")
    List<Map<String, Object>> getEwarnCountByType1CommId(@Param("commId") Integer commId,
                                                         @Param("startDateStr") String startDateStr,
                                                         @Param("endDateStr") String endDateStr);

    /**
     * @Desc: 统计在指定时间内全部商品的各预警级别的总数
     * @Param: [startDateStr, endDateStr]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/11/21 16:17
     */
    @Select("select case ppe.ewarn_level\n" +
            "           when 71 then 'red'\n" +
            "           when 72 then 'orange'\n" +
            "           when 73 then 'yellow'\n" +
            "           when 74 then 'green'\n" +
            "           end as levelType,\n" +
            "           count(1) as ewarnCount \n" +
            "from pss_comm_total comm\n" +
            "         left join pss_price_ewarn ppe on comm.comm_id = ppe.comm_id\n" +
            "where data_flag = 0\n" +
            "  and level_code = 3\n" +
            "  and date(ppe.ewarn_date) between #{startDateStr} and #{endDateStr}\n" +
            "group by ppe.ewarn_level")
    List<Map<String, Object>> getEwarnCount(@Param("startDateStr") String startDateStr,
                                            @Param("endDateStr") String endDateStr);

    /**
     * @Desc: 大屏-统计昨天涨幅排在前3名的，商品指定时间的价格信息
     * @Param: [commId, last180DayStr, lastDayStr]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssPriceEwarnEntity>
     * @Author: z.h.c
     * @Date: 2019/11/21 17:24
     */
    @Select("select ppe.*, comm.comm_name\n" +
            "from pss_price_ewarn ppe\n" +
            "         left join pss_comm_total comm\n" +
            "                   on ppe.comm_id = comm.comm_id\n" +
            "where ppe.comm_id = #{commId}\n" +
            "  and date(ppe.ewarn_date) between #{last180DayStr} and #{lastDayStr}\n" +
            "order by date(ppe.ewarn_date) asc")
    List<PssPriceEwarnEntity> getFirst3EwarnInfo(Integer commId, String last180DayStr, String lastDayStr);

    /**
     * @Desc: 大屏-统计昨天涨幅排在前3名的，商品指定时间的价格信息，支持按一级目录过滤
     * @Param: [commId, last180DayStr, lastDayStr, rootId]
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssPriceEwarnEntity>
     * @Author: z.l.c
     * @Date: 2019/11/21 17:24
     */
    @Select("<script>\n" +
            "select ppe.*, comm.comm_name\n" +
            "from pss_price_ewarn ppe\n" +
            "left join pss_comm_total comm on ppe.comm_id = comm.comm_id\n" +
            "left join pss_comm_total comm1 on comm.parent_code=comm1.comm_id\n" +
            "left join pss_comm_total comm2 on comm1.parent_code=comm2.comm_id\n" +
            "left join pss_comm_total comm3 on comm2.parent_code=comm3.comm_id\n" +
            "where ppe.comm_id = #{commId}\n" +
            "  and date(ppe.ewarn_date) between #{last180DayStr} and #{lastDayStr}\n" +
            "<if test='rootId!=null  '> " +
            "  and comm3.comm_id=#{rootId}" +
            "</if>" +
            "order by date(ppe.ewarn_date) asc\n" +
            "</script>")
    List<PssPriceEwarnEntity> getFirst3EwarnInfoNew(Integer commId, String last180DayStr, String lastDayStr, String rootId);

    @Select("SELECT ppw.*,pct.comm_name\n" +
            "FROM pss_price_ewarn ppw\n" +
            "left join pss_comm_total pct on ppw.comm_id=pct.comm_id\n" +
            "WHERE ppw.comm_id = #{p.commId}\n" +
            "  AND ppw.ewarn_type_id = #{p.ewarnTypeId}\n" +
            "  AND ppw.pric_type_id = #{p.pricTypeId}\n" +
            "  AND date(ppw.ewarn_date) BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY date(ppw.ewarn_date)")
    List<PssPriceEwarnEntity> queryPriceRangeByDate(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计各预警等级的商品数量
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT COUNT(1) cou,t.ewarn_level,m.code_name,n.cou1 total FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id,\n" +
            "(SELECT COUNT(1) cou1,t1.ewarn_level FROM pss_price_ewarn t1\n" +
            "WHERE date(t1.ewarn_date) = #{p.itrmDate}) n \n" +
            "WHERE date(t.ewarn_date) = #{p.itrmDate}\n" +
            "GROUP BY t.ewarn_level")
    List< Map<String, Object>> getCountByEwarmType(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计各预警等级的商品数量趋势
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT COUNT(1) cou,t.ewarn_level,DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') ewarn_date,m.code_name FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "WHERE date(t.ewarn_date) BETWEEN  #{p.startDate} AND  #{p.endDate}\n" +
            "GROUP BY t.ewarn_level,DATE_FORMAT(t.ewarn_date,'%Y-%m-%d')\n" +
            "ORDER BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') DESC\n")
    List< Map<String, Object>> getCountByEwarmTypeAndDate(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计涨幅前三商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.*,m.code_name,n.comm_name,p.pri_value y_pri_value FROM pss_price_ewarn t \n" +
            "              LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "              LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id \n" +
            "              LEFT JOIN (SELECT DISTINCT t1.comm_id, t1.pri_value,t1.pri_range,t1.ewarn_date FROM pss_price_ewarn t1\n" +
            "              WHERE DATE(t1.ewarn_date) = #{p.y_itrmDate} ) p\n" +
            "              ON p.comm_id = t.comm_id\n" +
            "            WHERE DATE(t.ewarn_date) = #{p.itrmDate}\n" +
            "            AND t.pri_range > 0\n" +
            "            GROUP BY t.comm_id\n" +
            "            ORDER BY t.pri_range DESC\n" +
            "            LIMIT 0,6")
    List< Map<String, Object>> getIncreaseThree(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计跌幅前三商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.*,m.code_name,n.comm_name,p.pri_value y_pri_value FROM pss_price_ewarn t \n" +
            "              LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "              LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id \n" +
            "              LEFT JOIN (SELECT DISTINCT t1.comm_id, t1.pri_value,t1.pri_range,t1.ewarn_date FROM pss_price_ewarn t1\n" +
            "              WHERE DATE(t1.ewarn_date) = #{p.y_itrmDate} ) p\n" +
            "              ON p.comm_id = t.comm_id\n" +
            "            WHERE DATE(t.ewarn_date) = #{p.itrmDate}\n" +
            "            AND t.pri_range < 0\n" +
            "            GROUP BY t.comm_id\n" +
            "            ORDER BY t.pri_range DESC\n" +
            "            LIMIT 0,6")
    List< Map<String, Object>> getDeclineThree(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品在各省份的价格信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.ewarn_id,t.comm_id,t.ewarn_date,t.stat_area_code,\n" +
            "t.pric_type_id,t.ewarn_type_id,t.ewarn_level,t.pri_value,\n" +
            "t.pri_range,t.unit,m.code_name FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id \n" +
            "WHERE DATE(t.ewarn_date) = #{p.itrmDate}\n" +
            "AND t.comm_id = #{p.commId}\n" +
            "AND t.stat_area_code IS NOT NULL\n" +
            "AND t.stat_area_code != ''\n" +
            "GROUP BY t.stat_area_code\n" +
            "ORDER BY t.pri_range DESC")
    List< Map<String, Object>> getPriceDistribution(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品在各省份的价格信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.ewarn_id,t.comm_id,t.ewarn_date,t.stat_area_code,\n" +
            "t.pric_type_id,t.ewarn_type_id,t.ewarn_level,t.pri_value,\n" +
            "t.pri_range,t.unit,m.code_name,n.comm_name FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id \n" +
            "LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id \n" +
            "WHERE DATE(t.ewarn_date) BETWEEN  #{p.startDate} AND #{p.endDate}\n" +
            "AND n.parent_code = #{p.commId}\n" +
            "AND t.stat_area_code in ('中国','全国') \n" +
            "AND t.stat_area_code IS NOT NULL\n" +
            "AND t.stat_area_code != ''\n" +
            "GROUP BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') \n" +
            "ORDER BY t.ewarn_date")
    List< Map<String, Object>> getEwarmInfoByDate(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品价格月趋势和周趋势信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.* FROM pss_price_ewarn t\n" +
            "WHERE t.stat_area_code = '全国'\n" +
            "AND t.comm_id = #{p.commId}\n" +
            "AND t.ewarn_date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY date(t.ewarn_date)\n" +
            "ORDER BY t.ewarn_date ")
    List<PssPriceEwarnEntity> getPriceThend(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品预测价格月趋势和周趋势信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.* ,m.comm_name FROM pss_price_relt t\n" +
            "LEFT JOIN pss_comm_total m ON m.comm_id = t.comm_id\n" +
            "WHERE t.fore_type = '日预测'\n" +
            "AND t.comm_id = #{p.commId}\n" +
            "AND t.fore_time BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.fore_time\n" +
            "ORDER BY t.fore_time ")
    List<PssPriceEwarnEntity> getForePriceThend(@Param("p") Map<String, Object> mp);
}
