package io.dfjinxin.modules.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
public interface PssRptInfoService extends IService<PssRptInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

