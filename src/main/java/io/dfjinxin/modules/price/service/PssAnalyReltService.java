package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.AnalyReqDto;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
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
public interface PssAnalyReltService extends IService<PssAnalyReltEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<PssAnalyReltEntity> getList(Map<String, Object> params);

    PssAnalyReltEntity selectByAnalyId(@Param("analyId") Integer analyId);

    Map runGenera(AnalyReqDto dto);
}

