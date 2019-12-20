package io.dfjinxin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;

import java.util.Map;

/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-14 16:04:09
 */
public interface PssRschConfService extends IService<PssRschConfEntity> {

    PageUtils queryPage(Map<String, Object> params);

//    //所有调度列表
//    PageUtils queryPageList();
}

