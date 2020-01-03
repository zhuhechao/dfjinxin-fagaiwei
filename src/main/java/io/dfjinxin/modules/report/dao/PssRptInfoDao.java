package io.dfjinxin.modules.report.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@Mapper
public interface PssRptInfoDao extends BaseMapper<PssRptInfoEntity> {

    IPage<PssRptInfoEntity> queryPage(Page page, @Param("param") Map<String, Object> map);

    /**
     * 分析报告单独接口
     * @return
     */
    List<PssRptInfoEntity> queryRptName(@Param("map") Map<String,Object> params);
}
