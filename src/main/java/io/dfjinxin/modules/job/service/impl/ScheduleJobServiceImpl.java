/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.job.dao.ScheduleJobDao;
import io.dfjinxin.modules.job.entity.ScheduleJobEntity;
import io.dfjinxin.modules.job.service.ScheduleJobService;
import io.dfjinxin.modules.job.utils.ScheduleUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {
	@Autowired
    private Scheduler scheduler;
	
	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	@Transactional(rollbackFor = Exception.class)
	public void init(){
		List<ScheduleJobEntity> scheduleJobList = this.list();
		for(ScheduleJobEntity scheduleJob : scheduleJobList){
			//指定日，月，季，年频率执行
			if(StringUtils.isNotBlank(scheduleJob.getCronExpression().trim())){
				CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
				//如果不存在，则创建
				if(cronTrigger == null) {
					ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
				}else {
					ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
				}
			}else{
				SimpleTrigger simpleTrigger = ScheduleUtils.getSimpleTrigger(scheduler, scheduleJob.getJobId());
				//如果不存在，则创建
				if(simpleTrigger == null) {
					ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
				}else {
					ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
				}
			}

		}
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String beanName = (String)params.get("beanName");

		IPage<ScheduleJobEntity> page = this.page(
			new Query<ScheduleJobEntity>().getPage(params),
			new QueryWrapper <ScheduleJobEntity>().like(StringUtils.isNotBlank(beanName),"bean_name", beanName)
		);

		return new PageUtils(page);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveJob(ScheduleJobEntity scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
        this.save(scheduleJob);
        
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ScheduleJobEntity scheduleJob) {
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
                
        this.updateById(scheduleJob);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.deleteScheduleJob(scheduler, jobId);
    	}
    	
    	//删除数据
    	this.removeByIds(Arrays.asList(jobIds));
	}

	@Override
    public int updateBatch(Long[] jobIds, int status){
    	Map<String, Object> map = new HashMap<>(2);
    	map.put("list", jobIds);
    	map.put("status", status);
    	return baseMapper.updateBatch(map);
    }
    
	@Override
	@Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
    	for(Long jobId : jobIds){
			ScheduleUtils.run(scheduler, this.getById(jobId));
    	}
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for(Long jobId : jobIds){
    		ScheduleUtils.pauseJob(scheduler, jobId);
    	}
        
    	updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.resumeJob(scheduler, jobId);
    	}

    	updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
    }
    
}
