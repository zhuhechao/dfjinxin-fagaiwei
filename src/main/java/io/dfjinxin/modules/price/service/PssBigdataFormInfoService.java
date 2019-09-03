package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssBigdataFormInfoDto;
import io.dfjinxin.modules.price.entity.PssBigdataFormInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 17:15:22
 */
public interface PssBigdataFormInfoService extends IService<PssBigdataFormInfoEntity> {

    List<PssBigdataFormInfoDto> listAll();
}

