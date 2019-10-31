/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjinxin.modules.job.entity.ScheduleJobEntity;

/**
 * 定时任务
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface CommonJobService extends IService<ScheduleJobEntity> {
    void saveScheJob(String rschId,String beanName,String params);
}
