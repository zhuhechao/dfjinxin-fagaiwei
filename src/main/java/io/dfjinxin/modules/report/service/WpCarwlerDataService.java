package io.dfjinxin.modules.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.report.entity.WpCarwlerDataEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
public interface WpCarwlerDataService extends IService<WpCarwlerDataEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

