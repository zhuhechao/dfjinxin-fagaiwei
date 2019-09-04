package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
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
 * @date 2019-09-02 17:03:24
 */
@Mapper
public interface PssAnalyReltDao extends BaseMapper<PssAnalyReltEntity> {
    Page queryPage(Page page, @Param("param")Map map);
}
