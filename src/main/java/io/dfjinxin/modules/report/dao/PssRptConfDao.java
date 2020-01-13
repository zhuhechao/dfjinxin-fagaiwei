package io.dfjinxin.modules.report.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-18 11:39:58
 */
@Mapper
public interface PssRptConfDao extends BaseMapper<PssRptConfEntity> {

    IPage<PssRptConfEntity> queryPage(Page page, @Param("param") Map<String, Object> map);

}
