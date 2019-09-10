package io.dfjinxin.modules.analyse.dao;

import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@Mapper
@Repository
public interface WpMcroIndexInfoDao extends BaseMapper<WpMcroIndexInfoEntity> {

    List<WpMcroIndexInfoEntity> selectIndexName();
}
