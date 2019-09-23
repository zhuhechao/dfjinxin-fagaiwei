package io.dfjinxin.modules.price.dao;

import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@Mapper
@Repository
public interface PssDatasetInfoDao extends BaseMapper<PssDatasetInfoEntity> {

    List<PssDatasetInfoEntity> getDataSetByAnalyWay(@Param("analyWay") String analyWay);
}
