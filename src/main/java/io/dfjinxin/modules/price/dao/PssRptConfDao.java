package io.dfjinxin.modules.price.dao;

import io.dfjinxin.modules.price.dto.PssRptInfoDto;
import io.dfjinxin.modules.price.entity.PssRptConfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:13:08
 */
@Mapper
public interface PssRptConfDao extends BaseMapper<PssRptConfEntity> {

    List<PssRptInfoDto> list(@Param("param") Map<String, Object> param);

    List<String> listTemplate();
}
