/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dfjinxin.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
@Repository
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
