package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.entity.WpCommPriOrgEntity;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WpCommPriOrgService
 * @Author：lym 863968235@qq.com
 * @Date： 2019/12/10 16:43
 * 修改备注：
 */
public interface WpCommPriOrgService extends IService<WpCommPriOrgEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WpCommPriOrgEntity> getData(Map<String,Object> params);
}
