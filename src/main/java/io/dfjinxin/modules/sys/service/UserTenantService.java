package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.entity.UserTenantEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户租户信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-12 17:06:08
 */
public interface UserTenantService extends IService<UserTenantEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<UserTenantEntity> queryAll();
}

