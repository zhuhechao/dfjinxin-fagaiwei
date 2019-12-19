package io.dfjinxin.modules.price.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.modules.price.entity.PssDataSourcesEntity;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @ClassName DataSourcesDao
 * @Author：lym 863968235@qq.com
 * @Date： 2019/9/19 17:02
 * 修改备注：
 */
@Mapper
@Repository
public interface PssDataSourcesDao extends BaseMapper<PssDataSourcesEntity> {



    IPage<PssDataSourcesEntity> queryPage(Page page, @Param("param") Map<String, Object> map);

}
