package io.dfjinxin.modules.price.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.price.dto.PssIndexReltDto;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 17:32:05
 */
public interface PssIndexReltService extends IService<PssIndexReltEntity> {

    List<PssIndexReltDto> list(String indexName, Date dateFrom, Date dateTo);
}

