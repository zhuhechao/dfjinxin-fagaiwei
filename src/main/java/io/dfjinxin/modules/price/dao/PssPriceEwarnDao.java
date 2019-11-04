package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
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

    List<PssPriceEwarnEntity> queryEchartsData(@Param("commId") Integer commId,
                                               @Param("ewarnTypeId") Integer ewarnTypeId,
                                               @Param("startDateStr") String startDateStr,
                                               @Param("endDateStr") String endDateStr);

    List queryType3Warn();

    List<PssPriceEwarnEntity> queryEwarnlevel();

    PssPriceEwarnEntity selectMaxRange(
            @Param("commId") Integer commId,
            @Param("dateStr") String dateStr);

//    PssPriceEwarnEntity selectMaxDateTimeEntiey(@Param("commId") Integer commId);

    List<PssPriceEwarnEntity> queryPriceEwarnByCommId(@Param("commId") Integer commId);

    List<PssPriceEwarnEntity> queryPriceEwarnByDate(@Param("commId") Integer commId,
                                                    @Param("dateStr") String dateStr);

    List<Map<Integer, Object>> countEwarn(@Param("dateStr") String dateStr);

    @Select("select pl.comm_name,pl.comm_id,pn.ewarn_date, pn.pri_range from pss_price_ewarn pn " +
            "LEFT JOIN (select * from pss_comm_total pl where pl.parent_code=#{params.commId}) pl " +
            "on pn.comm_id=pl.comm_id " +
            "where pn.ewarn_date between STR_TO_DATE(#{params.beginYestarday},'%Y-%m-%d %H:%i:%S') " +
            " and STR_TO_DATE(#{params.endYestarday},'%Y-%m-%d %H:%i:%S') "+
            "and pl.comm_id is not null")
    List<Map<String, Object>> getDayReport(@Param("params") Map<String,Object> params);

}
