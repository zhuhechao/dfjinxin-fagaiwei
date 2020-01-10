package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@Mapper
@Repository
public interface PssDatasetInfoDao extends BaseMapper<PssDatasetInfoEntity> {

    List<PssDatasetInfoEntity> getDataSetByAnalyWay(@Param("analyWay") String analyWay);

    Page queryByPage(Page page, @Param("param") Map map);

    @Select("SELECT *\n" +
            "FROM pss_dataset_info where data_set_eng_name is not null\n" +
            "and data_set_eng_name <> ''")
    List<PssDatasetInfoEntity> getDataSetList();
}
