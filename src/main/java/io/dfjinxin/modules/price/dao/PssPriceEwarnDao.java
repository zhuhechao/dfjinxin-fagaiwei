package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import io.dfjinxin.modules.price.dto.AreaPrice;
import io.dfjinxin.modules.price.dto.ChinaAreaInfo;
import io.dfjinxin.modules.price.dto.PwwPriceEwarnDto;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import java.util.Set;
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

    @Select("SELECT ppw.comm_id,ppw.ewarn_id,ppw.ewarn_date,\n" +
            "ppw.ewarn_level,ppw.ewarn_type_id,ppw.pric_type_id,\n" +
            "ppw.pri_range,ppw.pri_value,ppw.stat_area_code,ppw.unit,pct.comm_name\n" +
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
    @Select("SELECT COUNT(1) cou,f.ewarn_level,f.code_name ,f1.cou total FROM (\n" +
            "SELECT t1.ewarn_level ,m.code_name\n" +
            "FROM pss_price_ewarn t1\n" +
            "LEFT JOIN wp_ascii_info m ON t1.ewarn_level = m.code_id\n" +
            "            WHERE date(t1.ewarn_date) = #{p.itrmDate}\n" +
            "\t\t\t\t\t\tAND t1.stat_area_code in ('全国','中国')\n" +
            "GROUP BY t1.comm_id) f,\n" +
            "(SELECT COUNT(DISTINCT t2.comm_id) cou FROM pss_price_ewarn t2\n" +
            "            WHERE date(t2.ewarn_date) = #{p.itrmDate}\n" +
            "\t\t\t\t\t\tAND t2.stat_area_code in ('全国','中国')\n" +
            ") f1\n" +
            "GROUP BY f.ewarn_level\n" +
            "ORDER BY f.ewarn_level ")
    List< Map<String, Object>> getCountByEwarmType(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.comm_id,tt.comm_name,t.ewarn_date,t.ewarn_level,t.pri_value,t.pri_range,t.unit FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total tt ON tt.comm_id = t.comm_id\n" +
            "WHERE t.stat_area_code in ('全国','中国')\n" +
            "AND date(t.ewarn_date) = #{p.itrmDate}\n" +
            "AND t.ewarn_level = #{p.ewarnLevel}  \n" +
            "GROUP BY t.comm_id\n" +
            "ORDER BY t.pri_range DESC")
    List< Map<String, Object>> getEwarmIndexList(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计各预警等级的商品数量趋势
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT COUNT(1) cou,f.code_name,f.ewarn_date,f.ewarn_level FROM (\n" +
            "SELECT t.comm_id,t.ewarn_level,DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') ewarn_date,m.code_name \n" +
            "FROM pss_price_ewarn t\n" +
            "            LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "            WHERE date(t.ewarn_date) BETWEEN  #{p.startDate} AND  #{p.endDate}\n" +
            "            AND t.stat_area_code in ('全国','中国')\n" +
            "           GROUP BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d'),t.comm_id\n" +
            ") f\n" +
            "GROUP BY f.ewarn_level,f.ewarn_date\n" +
            "ORDER BY f.ewarn_date DESC\n")
    List< Map<String, Object>> getCountByEwarmTypeAndDate(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计涨幅前6商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,t.ewarn_id,t.ewarn_date,\n" +
            "            t.ewarn_level,t.ewarn_type_id,t.pric_type_id,\n" +
            "            t.pri_range,t.pri_value,t.stat_area_code,t.unit,\n" +
            "           m.code_name,n.comm_name,f.comm_id shangpinId,f.comm_name shangpinName,f.img_name\n" +
            "FROM pss_price_ewarn t \n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id\n" +
            "LEFT JOIN (SELECT t1.comm_id,t1.comm_name,t2.img_name FROM pss_comm_total t1\n" +
            "           LEFT JOIN pss_comm_total_img t2 ON t1.comm_id = t2.comm_id) f ON f.comm_id = n.parent_code\n" +
            "           WHERE t.pri_range > 0\n" +
            "           AND t.pri_value >0\n" +
            "           AND t.stat_area_code in ('全国','中国') \n" +
            "            AND t.stat_area_code IS NOT NULL \n" +
            "                         AND f.img_name IS NOT NULL \n" +
            "                         AND f.img_name != ''\n" +
            "                       AND t.ewarn_date BETWEEN #{p.smaDate} AND #{p.emaDate}\n" +
            "                        GROUP BY t.comm_id\n" +
            "                        ORDER BY t.pri_range DESC\n" +
            "                        LIMIT 0,6")
    List< Map<String, Object>> getIncreaseThree(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计跌幅前6商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,t.ewarn_id,t.ewarn_date,\n" +
            "            t.ewarn_level,t.ewarn_type_id,t.pric_type_id,\n" +
            "            t.pri_range,t.pri_value,t.stat_area_code,t.unit,\n" +
            "           m.code_name,n.comm_name,f.comm_id shangpinId,f.comm_name shangpinName,f.img_name\n" +
            "FROM pss_price_ewarn t \n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id\n" +
            "LEFT JOIN (SELECT t1.comm_id,t1.comm_name,t2.img_name FROM pss_comm_total t1\n" +
            "           LEFT JOIN pss_comm_total_img t2 ON t1.comm_id = t2.comm_id) f ON f.comm_id = n.parent_code\n" +
            "           WHERE t.pri_range <0 \n" +
            "           AND t.pri_value >0\n" +
            "          AND t.stat_area_code in ('全国','中国') \n" +
            "            AND t.stat_area_code IS NOT NULL \n" +
            "                         AND f.img_name IS NOT NULL \n" +
            "                         AND f.img_name != ''\n" +
            "                       AND t.ewarn_date BETWEEN #{p.smaDate} AND #{p.emaDate}\n" +
            "                        GROUP BY t.comm_id\n" +
            "                        ORDER BY ABS(t.pri_range) DESC\n" +
            "                       ")
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
            "WHERE DATE(t.ewarn_date)  BETWEEN  '2020-11-20' AND #{p.itrmDate}\n" +
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
    @Select("SELECT n.comm_name,t.comm_id,DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') date,\n" +
            "t.ewarn_id,t.ewarn_level,t.pri_range,t.pri_value,t.pri_yonyear,t.pric_type_id index_id,fo.index_name,t.unit  \n" +
            "FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id\n" +
            "LEFT JOIN wp_base_index_info fo ON fo.index_id = t.pric_type_id\n" +
            "            WHERE  n.parent_code = #{p.commId}\n" +
            "AND t.ewarn_date BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            " GROUP BY date(t.ewarn_date),t.pric_type_id\n" +
            "ORDER BY t.ewarn_date ")
    List<Map<String, Object>> getPriceThend(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品预警在各省分布
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    List<Map<String, Object>> getEwarnProvince(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品预警在各省分布
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,ps.comm_name,t.stat_area_code,t.ewarn_id,max(t.ewarn_date) ewarn_date, \n" +
            "t.ewarn_level,t.ewarn_type_id,t.pric_type_id,\n" +
            " t.pri_range,t.pri_value,t.unit\n" +
            "FROM pss_price_ewarn t \n" +
            "left join pss_comm_total ps on ps.comm_id = t.comm_id\n" +
            "where t.comm_id = #{p.commId}\n" +
            "and t.ewarn_date = #{p.ewarnDate}\n" +
            "and t.stat_area_code  = #{p.ewarnProvince}\n" +
            "limit 1")
    List<Map<String, Object>> getEwarnProvinceInfo(@Param("p") Map<String, Object> mp);


    @Select("SELECT t.comm_id,m.comm_name\n" +
            "                         FROM pss_price_relt t\n" +
            "                        LEFT JOIN pss_comm_total m ON m.comm_id = t.comm_id                      \n" +
            "                        WHERE t.fore_type = '日预测'\n" +
            "AND m.parent_code = #{p.commId}\n" +
            "AND DATE(t.fore_time) BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.comm_id")
    List<Map<String, Object>> getForePriceThendCommIds(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.index_id,m.index_name\n" +
            "                         FROM pss_price_relt t\n" +
            "                        LEFT JOIN wp_base_index_info m ON m.index_id = t.index_id \n" +
            "                        WHERE t.fore_type = '日预测'\n" +
            "AND t.comm_id = #{p.indexId}\n" +
            "AND DATE(t.fore_time) BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.index_id")
    List<Map<String, Object>> getForePriceThendIndexs(@Param("p") Map<String, Object> mp);


    /**
     * @Desc: 获取指定商品预测价格月趋势和周趋势信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT fo.index_name,t.index_id,DATE_FORMAT(t.fore_time,'%Y-%m-%d') date,t.fore_price value\n" +
            "                         FROM pss_price_relt t\n" +
            "                       LEFT JOIN wp_base_index_info fo ON fo.index_id = t.index_id\n" +
            "            WHERE t.fore_type = '日预测'\n" +
            "AND t.index_id = #{p.indexId}\n" +
            "AND t.fore_price >0\n" +
            "AND DATE_FORMAT(t.fore_time,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY DATE_FORMAT(t.fore_time,'%Y-%m-%d')\n" +
            "ORDER BY DATE_FORMAT(t.fore_time,'%Y-%m-%d')")
    List<Map<String, Object>> getForePriceThend(@Param("p") Map<String, Object> mp);


    @Select("SELECT t.stat_area_code FROM pss_price_ewarn t\n" +
            "WHERE t.stat_area_code IS NOT NULL \n" +
            " AND DATE(t.ewarn_date)  BETWEEN '2020-11-20' AND  #{p.endDate}\n" +
            "AND t.stat_area_code in  (SELECT w.area_name FROM wp_area_info w WHERE w.area_id BETWEEN 1 AND 32) \n" +
            "GROUP  BY t.stat_area_code")
    List<Map<String, Object>> getProvince(@Param("p") Map<String, Object> mp);

    @Select("SELECT m.code_name,t.stat_area_code,t.ewarn_date,t.ewarn_level FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
            "WHERE t.comm_id = #{p.commId}\n" +
            " AND DATE(t.ewarn_date)    BETWEEN '2020-11-20' AND  #{p.itrmDate}\n" +
            "AND t.stat_area_code in  (SELECT w.area_name FROM wp_area_info w WHERE w.area_id BETWEEN 1 AND 32 or w.parent_id  BETWEEN 1 AND 32 ) \n" +
            "GROUP BY t.ewarn_level,t.stat_area_code\n")
    List<Map<String, Object>> getProvinceByCommId(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计个省份涨幅前三商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
//    @Select("SELECT t.comm_id,t.ewarn_id,t.ewarn_date,\n" +
//            "t.ewarn_level,t.ewarn_type_id,t.pric_type_id,\n" +
//            "t.pri_range,t.pri_value,t.stat_area_code,t.unit,\n" +
//            "m.code_name,n.comm_name,p.pri_value y_pri_value,\n" +
//            "g.img_name,s.comm_id shangpinId,s.comm_name shangpinName FROM pss_price_ewarn t \n" +
//            "                                      LEFT JOIN wp_ascii_info m ON t.ewarn_level = m.code_id\n" +
//            "                                     LEFT JOIN pss_comm_total n ON n.comm_id = t.comm_id\n" +
//            "                                      LEFT JOIN (SELECT DISTINCT t1.comm_id, t1.pri_value,\n" +
//            "                                       t1.pri_range,t1.ewarn_date FROM pss_price_ewarn t1\n" +
//            "                                      WHERE DATE(t1.ewarn_date)  BETWEEN #{p.satrtDate} AND  #{p.endDate}) p\n" +
//            "                                      ON p.comm_id = t.comm_id\n" +
//            "            LEFT JOIN pss_comm_total_img g ON n.parent_code = g.comm_id \n" +
//            "\t\t\t\t\t\tLEFT JOIN pss_comm_total s ON g.comm_id = s.comm_id \n" +
//            "                                    WHERE DATE(t.ewarn_date)  BETWEEN #{p.satrtDate} AND   #{p.endDate}\n" +
//            "                                   AND t.stat_area_code = #{p.province} \n" +
//            "                                        AND  0 <  t.pri_range\n" +
//            "             AND g.img_name IS NOT NULL \n" +
//            "             AND g.img_name != '' \n" +
//            "            AND t.stat_area_code in  (SELECT w.area_name FROM wp_area_info w WHERE w.area_id BETWEEN 1 AND 32) \n" +
//            "                                    GROUP BY t.comm_id\n" +
//            "                                    ORDER BY t.pri_range DESC\n" +
//            "                                    LIMIT 0,3")
//    List< Map<String, Object>> getUpThree(@Param("p") Map<String, Object> mp);
    @Select("SELECT t.comm_id,ps.comm_name,MAX(t.pri_range) pri_range,t.pri_value,t.unit,\n" +
            "t.stat_area_code,pc.comm_name shangpinName,pc.comm_id shangpinId ,pm.img_name\n" +
            "FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total ps ON ps.comm_id = t.comm_id\n" +
            "LEFT JOIN pss_comm_total pc ON pc.comm_id = ps.parent_code\n" +
            "LEFT JOIN pss_comm_total_img pm ON pm.comm_id = pc.comm_id\n" +
            "WHERE ps.level_code = 3\n" +
            "AND t.pri_range >0\n" +
            "AND t.stat_area_code = #{p.province}\n" +
            "AND DATE(t.ewarn_date)  BETWEEN #{p.satrtDate} AND   #{p.endDate}\n" +
            "GROUP BY t.comm_id\n" +
            "ORDER BY t.pri_range DESC\n" +
            "LIMIT 0,3")
    List< Map<String, Object>> getUpThree(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 统计个省份跌幅前三商品
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,ps.comm_name,-MAX(ABS(t.pri_range)) pri_range,t.pri_value,t.unit,\n" +
            "t.stat_area_code,pc.comm_name shangpinName,pc.comm_id shangpinId ,pm.img_name\n" +
            "FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total ps ON ps.comm_id = t.comm_id\n" +
            "LEFT JOIN pss_comm_total pc ON pc.comm_id = ps.parent_code\n" +
            "LEFT JOIN pss_comm_total_img pm ON pm.comm_id = pc.comm_id\n" +
            "WHERE ps.level_code = 3\n" +
            "AND t.pri_range <0\n" +
            "AND t.stat_area_code = #{p.province}\n" +
            "AND DATE(t.ewarn_date)  BETWEEN #{p.satrtDate} AND   #{p.endDate}\n" +
            "GROUP BY t.comm_id\n" +
            "ORDER BY ABS(t.pri_range) DESC\n" +
            "LIMIT 0,3")
    List< Map<String, Object>> getDownThree(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.comm_id,m.comm_name\n" +
            "             FROM pss_price_ewarn t\n" +
            "            LEFT JOIN pss_comm_total m ON t.comm_id = m.comm_id\n" +
            "WHERE  DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}  \n" +
            "AND m.parent_code = #{p.commId}\n" +
            "GROUP BY t.comm_id\n" +
            "ORDER BY t.comm_id ")
    List< Map<String, Object>> getThendCommList(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.pric_type_id index_id,m.index_name\n" +
            "                        FROM pss_price_ewarn t\n" +
            "                        LEFT JOIN wp_base_index_info m ON t.pric_type_id = m.index_id\n" +
            "WHERE  DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}  \n" +
            "AND t.comm_id = #{p.id}\n" +
            "GROUP BY t.pric_type_id\n" +
            "ORDER BY t.pric_type_id ")
    List< Map<String, Object>> getThendGuiCommList(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.pric_type_id index_id,t.pri_value,t.pri_range value,t.unit,fo.index_name,\n" +
            "DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') date\n" +
            "                         FROM pss_price_ewarn t\n" +
            "                       LEFT JOIN wp_base_index_info fo ON fo.index_id = t.pric_type_id\n\n" +
            "WHERE  DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}  \n" +
            "AND t.pric_type_id = #{p.indexId}\n" +
            "AND t.pri_value>0\n" +
            "GROUP BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d')\n" +
            "ORDER BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') ")
    List< Map<String, Object>> getThendCommData(@Param("p") Map<String, Object> mp);


    /**
     * @Desc: 当前价格预警规格品集合
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,m.comm_name\n" +
            "             FROM pss_price_ewarn t\n" +
            "            LEFT JOIN pss_comm_total m ON t.comm_id = m.comm_id\n" +
            "WHERE  DATE_FORMAT(t.ewarn_date,'%Y') = #{p.year}  \n" +
            "AND m.parent_code = #{p.commId}\n" +
            "GROUP BY t.comm_id\n" +
            "ORDER BY t.comm_id ")
    List< Map<String, Object>> getThisYearErawCommList(@Param("p") Map<String, Object> mp);


    /**
     * @Desc: 当前价格预警规格品指标集合
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.pric_type_id index_id,fo.index_name\n" +
            "             FROM pss_price_ewarn t\n" +
            "            LEFT JOIN pss_comm_total m ON t.comm_id = m.comm_id\n" +
            "            LEFT JOIN wp_base_index_info fo ON fo.index_id = t.pric_type_id\n" +
            "            WHERE  DATE_FORMAT(t.ewarn_date,'%Y')  = #{p.year} \n" +
            "            AND t.comm_id = #{p.commId}\n" +
            "            GROUP BY t.pric_type_id\n" +
            "            ORDER BY t.pric_type_id desc")
    List< Map<String, Object>> getThisYearErawIndexList(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取当前价格预警规格品预警区间值
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.ewarn_type_id,cf.ewarn_llmt_yellow,cf.ewarn_ulmt_yellow,\n" +
            "cf.ewarn_llmt_orange,cf.ewarn_ulmt_orange,cf.ewarn_llmt_red,cf.ewarn_ulmt_red\n" +
            "                         FROM pss_price_ewarn t\n" +
            "                        LEFT JOIN pss_comm_total m ON t.comm_id = m.comm_id\n" +
            "LEFT JOIN pss_ewarn_conf cf ON cf.ewarn_type_id = t.ewarn_type_id\n" +
            "            WHERE  DATE_FORMAT(t.ewarn_date,'%Y')  = #{p.year} \n" +
            "            AND m.parent_code = #{p.commId}\n" +
            "            GROUP BY t.ewarn_type_id\n" +
            "LIMIT 1")
    List< Map<String, Object>> getEwarValue(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 当前价格预警指标数据集合
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') date,t.pric_type_id index_id,concat(fo.index_name, '--', t.stat_area_code) index_name,\n" +
            "            t.unit,t.pri_range,t.pri_yonyear\n" +
            "            FROM pss_price_ewarn t\n" +
            "            LEFT JOIN pss_comm_total m ON t.comm_id = m.comm_id\n" +
            "            LEFT JOIN wp_base_index_info fo ON fo.index_id = t.pric_type_id\n" +
            "WHERE DATE_FORMAT(t.ewarn_date,'%Y') = #{p.year} \n" +
            "AND t.pric_type_id = #{p.indexId} \n" +
            "GROUP BY DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') \n" +
            "ORDER BY  DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') ")
    List< Map<String, Object>> warningIndexDate(@Param("p") Map<String, Object> mp);


    /**
     * @Desc: 价格长期预测
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id,t.fore_time date,t.fore_price,t.actual_price,m.comm_name, i.index_name, i.unit FROM pss_price_relt t\n" +
            "LEFT JOIN pss_comm_total m\n" +
            "ON m.comm_id = t.comm_id \n" +
            "LEFT JOIN wp_base_index_info i\n" +
            "on i.index_id = t.index_id\n"+
            "WHERE t.fore_type = '年预测'\n" +
            "And t.index_id = #{p.indexId}\n" +
            "AND t.fore_price >0\n" +
            "AND fore_time >= #{p.satrtYear}\n" +
            "GROUP BY fore_time \n" +
            "ORDER BY fore_time ")
    List< Map<String, Object>> getYearFore(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 价格长期预测
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.index_id,n.index_name\n" +
            "            FROM pss_price_relt t\n" +
            "            LEFT JOIN pss_comm_total p ON p.comm_id = t.comm_id\n" +
            "LEFT JOIN wp_base_index_info n ON n.index_id = t.index_id\n" +
            "            WHERE t.fore_type = #{p.foreType}\n" +
            "            AND (p.parent_code = #{p.commId} OR t.comm_id = #{p.commId})\n" +
            "            AND t.fore_time  >= #{p.satrtDate1}\n" +
            "AND n.index_name IS NOT NULL\n" +
            "            GROUP BY t.index_id ")
    List< Map<String, Object>> getForeIndex(@Param("p") Map<String, Object> mp);


    /**
     * @Desc: 价格短期预测
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.index_id,n.index_name ,DATE_FORMAT(t.fore_time,'%Y-%m-%d') date,t.fore_price value FROM pss_price_relt t\n" +
            "            LEFT JOIN wp_base_index_info n ON n.index_id = t.index_id\n" +
            "            WHERE t.fore_type = '日预测'\n" +
            "            AND t.index_id = #{p.indexId}\n" +
            "AND DATE_FORMAT(t.fore_time,'%Y-%m-%d') >= #{p.satrtDate1} \n" +
            "            GROUP BY DATE_FORMAT(t.fore_time,'%Y-%m-%d')\n" +
            "            ORDER BY DATE_FORMAT(t.fore_time,'%Y-%m-%d')")
    List< Map<String, Object>> getDayFore(@Param("p") Map<String, Object> mp);


    @Select("SELECT t.comm_id,p.comm_name,t.pri_range,t.pri_value,t.ewarn_level,n.code_name,t.unit," +
            "DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') date,fo.index_name" +
            " FROM pss_price_ewarn t\n" +
            "LEFT JOIN wp_ascii_info n ON t.ewarn_level = n.code_id\n" +
            "LEFT JOIN pss_comm_total p ON p.comm_id = t.comm_id \n" +
            "LEFT JOIN wp_base_index_info fo ON fo.index_id = t.pric_type_id\n" +
            "WHERE  t.comm_id = #{p.commId}\n" +
            "AND t.stat_area_code = #{p.areaName}\n" +
            "AND t.pri_range = #{p.val}\n" +
            "ORDER BY t.ewarn_date desc\n" +
            "LIMIT 1")
    List< Map<String, Object>> getCommIdByArea(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.comm_id ,p.comm_name,t.pri_range,t.pri_value,t.unit,t.ewarn_date," +
            "t.ewarn_level,w.code_name,t.pri_yonyear,t.stat_area_code  " +
            "FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total p ON t.comm_id = p.comm_id\n" +
            "LEFT JOIN wp_ascii_info w ON w.code_id = t.ewarn_level\n" +
            "WHERE t.comm_id = #{p.commId}\n" +
            "AND DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "ORDER BY ABS(t.pri_range) DESC\n" +
            "LIMIT 1")
    Map<String, Object> getMaxPro(@Param("p") Map<String, Object> mp);

    @Select("SELECT t.stat_area_code,MAX(t.pri_range) maxPro,MIN(t.pri_range) minPro FROM pss_price_ewarn t\n" +
            "WHERE t.stat_area_code in (SELECT a.area_name FROM wp_area_info a WHERE a.area_id BETWEEN 1 AND 32 or  a.parent_id BETWEEN 1 AND 32 )\n" +
            "AND t.comm_id = #{p.commId}\n" +
            "AND DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.stat_area_code")
    List< Map<String, Object>> getAreaByCommId(@Param("p") Map<String, Object> mp);


    @Select( "<script>" +
            " SELECT DISTINCT w2.area_name areaName ,w1.area_name contryName FROM wp_area_info w1 JOIN wp_area_info w2 ON (w1.area_id = w2.parent_id OR w1.area_id = w2.area_id) AND w1.area_id BETWEEN 1 AND 32 and w2.area_name in ( " +
            " <foreach collection =\"areas\" item=\"item\" index= \"index\" separator =\",\">" +
            "#{item} " +
            "</foreach>"    +
            " ) " +
            "</script>"   )
    List< Map<String, String>> getProArea(@Param("areas") List<String> areas);

    @Select("SELECT t.stat_area_code,MAX(t.pri_range) maxPro,MIN(t.pri_range) minPro FROM pss_price_ewarn t\n" +
            "WHERE t.stat_area_code in (SELECT a.area_name FROM wp_area_info a WHERE a.parent_id BETWEEN 1 AND 32)\n" +
            "AND t.comm_id = #{p.commId}\n" +
            "AND DATE_FORMAT(t.ewarn_date,'%Y-%m-%d') BETWEEN #{p.startDate} AND #{p.endDate}\n" +
            "GROUP BY t.stat_area_code")
    List< Map<String, Object>> getProAreaByCommId(@Param("p") Map<String, Object> mp);

    /**
     * @Desc: 获取指定商品预测价格月趋势和周趋势信息
     * @Param: [itrmDate]
     * @Author: y.b
     * @Date: 2020.11.16
     */
    @Select("SELECT t.comm_id ,p.comm_name FROM pss_price_ewarn t\n" +
            "LEFT JOIN pss_comm_total p ON t.comm_id = p.comm_id\n" +
            "WHERE p.parent_code = #{p.commId}\n" +
            "AND t.ewarn_date BETWEEN #{p.smaDate} AND #{p.emaDate}\n" +
            "GROUP BY t.comm_id")
    List<Map<String, Object>> getMaxProCommid(@Param("p") Map<String, Object> mp);

    /***
     * @Author LiangJianCan
     * @Description  获取全中国城市的信息
     * @Date 2021/4/21 15:17
     * @Param []
     * @return java.util.List<io.dfjinxin.modules.price.dto.ChinaAreaInfo>
     **/
    @Select("select parent_id as parentId , area_name as areaName , area_id as areaId "
            + "from wp_area_info where area_type = 0")
    List<ChinaAreaInfo> getChinaAreaInfo();

    /***
     * @Author LiangJianCan
     * @Description  通过commid,地域名称和日期查询各省份和对应的价格
     * @Date 2021/4/21 15:55
     * @Param [commId, areaName, date]
     * @return java.util.List<io.dfjinxin.modules.price.dto.AreaPrice>
     **/
    List<AreaPrice> getAreaPrice(@Param("commId")String commId , @Param("areaName")Set<String> areaName ,@Param("date")String date);

    /***
     * @Author LiangJianCan
     * @Description  获取最近有记录的日期
     * @Date 2021/4/25 9:42
     * @Param []
     * @return java.lang.String
     **/
    String getLastRecordDate(@Param("commId")String commId );
}
