package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@Mapper
@Repository
public interface PssAnalyInfoDao extends BaseMapper<PssAnalyInfoEntity> {
    List<PssAnalyInfoEntity> getAnalyInfo(@Param("param")Map map);
}
