package io.dfjinxin.modules.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.report.dto.PssRptConfDto;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-18 11:39:58
 */
public interface PssRptConfService extends IService<PssRptConfEntity> {

    PageUtils queryPage(Map<String, Object> params);
    PssRptConfDto saveOrUpdate(PssRptConfDto dto);
}

