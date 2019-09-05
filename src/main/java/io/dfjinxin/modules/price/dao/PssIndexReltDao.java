package io.dfjinxin.modules.price.dao;

import io.dfjinxin.modules.price.dto.PssIndexReltDto;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 17:32:05
 */
@Mapper
public interface PssIndexReltDao extends BaseMapper<PssIndexReltEntity> {

    List<PssIndexReltDto> list(@Param("indexName") String indexName, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);
}
