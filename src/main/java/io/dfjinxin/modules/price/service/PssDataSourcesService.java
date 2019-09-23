package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.PssDataSourcesEntity;

import java.util.Map;

/**
 * @ClassName DataSourcesService
 * @Author：lym 863968235@qq.com
 * @Date： 2019/9/19 16:57
 * 修改备注：
 */
public interface PssDataSourcesService extends IService<PssDataSourcesEntity> {

    /**
     * 数据源管理查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

}
