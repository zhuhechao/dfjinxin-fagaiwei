package io.dfjinxin.modules.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.model.entity.ModelInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 模型基本信息
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-25 13:48:01
 */
@Mapper
public interface ModelInfoDao extends BaseMapper<ModelInfoEntity> {

    Page queryByPage(Page page, @Param("param") Map map);
}
