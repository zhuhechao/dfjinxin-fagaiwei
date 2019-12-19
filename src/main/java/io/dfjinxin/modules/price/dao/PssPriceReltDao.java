package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.price.dto.PssPriceReltDto;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@Mapper
@Repository
public interface PssPriceReltDao extends BaseMapper<PssPriceReltEntity> {

    IPage<PssPriceReltEntity> queryPage(Page page, @Param("param") Map map);

    @Select("select relt.*, com.comm_name\n" +
            "from pss_price_relt relt\n" +
            "         left join pss_comm_total com\n" +
            "                   on relt.comm_id = com.comm_id\n" +
            "where relt.comm_id =   #{commId}\n" +
            "  and relt.fore_type = #{foreType}\n" +
            "  and relt.data_date > #{dataDate}\n" +
            "order by data_date asc\n" +
            "limit 0,#{endIndex}")
    List<PssPriceReltEntity> selectByForeType(@Param("commId") Integer commId,
                                              @Param("foreType") String foreType,
                                              @Param("dataDate") String dataDate,
                                              @Param("endIndex") Integer endIndex);

    @Select("select relt.*, com.comm_name\n" +
            "from pss_price_relt relt\n" +
            "         left join pss_comm_total com\n" +
            "                   on relt.comm_id = com.comm_id\n" +
            "where relt.comm_id =   #{commId}\n" +
            "  and relt.fore_type = #{foreType}\n" +
            "  and relt.data_date between #{startDate} and #{endDate}\n" +
            "order by data_date asc")
    List<PssPriceReltEntity> selectByDate(@Param("commId") Integer commId,
                                          @Param("foreType") String foreType,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);


    @Select("SELECT ppr.*, pct.comm_name, pfmr.mod_name, pfmr.algo_name\n" +
            "        FROM pss_price_relt ppr\n" +
            "        LEFT JOIN pss_comm_total pct ON ppr.comm_id = pct.comm_id\n" +
            "        LEFT JOIN pss_dataset_info pdi ON ppr.data_set_id = pdi.data_set_id\n" +
            "        LEFT JOIN pss_fore_mod_result pfmr ON ppr.mod_id = pfmr.mod_id\n" +
            "        WHERE ppr.comm_id in (select tmp.comm_id from pss_comm_total tmp\n" +
            "        where data_flag=0 and tmp.parent_code = #{commId})\n" +
            "order by data_date desc\n" +
            "limit 0,1")
    PssPriceReltDto queryCommByCommId(@Param("commId") Integer commId);

    IPage<PssPriceReltEntity> getDataGrid(Page page, @Param("param") Map map);
}
