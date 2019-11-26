package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            "  and relt.fore_time > #{foreTime}\n" +
            "order by fore_time desc\n" +
            "limit 0,#{endIndex}")
    List<PssPriceReltEntity> selectByForeType(@Param("commId") Integer commId,
                                              @Param("foreType") String foreType,
                                              @Param("foreTime") String foreTime,
                                              @Param("endIndex") Integer endIndex);
}
