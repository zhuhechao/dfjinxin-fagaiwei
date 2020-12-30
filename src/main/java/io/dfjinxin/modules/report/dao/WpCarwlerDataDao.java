package io.dfjinxin.modules.report.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.entity.WpCarwlerDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface WpCarwlerDataDao extends BaseMapper<WpCarwlerDataEntity> {


    @Select("SELECT t.title rptName,t.data_dt crteTime,t.link,t.web,t.comm_name commName,t.id rptId,t.report_flag  FROM wp_crawler_data t\n" +
            "WHERE t.title like #{p.name}\n" +
            "GROUP BY t.link")
    IPage<Map<String, Object>> queryPage(Page page, @Param("p") Map<String, Object> map);
}
