package io.dfjinxin.modules.analyse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@Mapper
@Repository
public interface WpBaseIndexValDao extends BaseMapper<WpBaseIndexValEntity> {
    List<Map<String, Object>> queryIndexTypeByCommId(@Param("commId") Integer commId);

    List<Map<String, Object>> queryIndexTypeByCondition(@Param("condition") Map<String, Object> condition);

    List<Map<String, Object>> queryIndexTypePrice(@Param("condition") Map<String, Object> condition);

    List<WpBaseIndexValEntity> selectListBystatAreaId(@Param("commId")int commId,
                                                      @Param("indexType")String indexType,
                                                      @Param("indexId")Integer indexId);

    List<WpBaseIndexValEntity> queryByIndexType(@Param("commId")Integer commId, @Param("indexType")String indexType);

    List<WpBaseIndexValEntity> queryMapValByIndexType(@Param("commId")Integer commId );

    List<WpBaseIndexValEntity> queryNoPriceByIndexType(@Param("commId")Integer commId, @Param("indexType")String indexType);

    IPage<PssPriceReltEntity> queryPageByDate(Page page, @Param("param") Map map);

}
