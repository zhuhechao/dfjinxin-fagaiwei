package io.dfjinxin.modules.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.job.entity.ScheduleJobEntity;
import io.dfjinxin.modules.job.service.ScheduleJobService;
import io.dfjinxin.modules.report.dao.PssRptConfDao;
import io.dfjinxin.modules.report.dto.PssRptConfDto;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.dfjinxin.modules.sys.service.PssRschConfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("pssRptConfService")
public class PssRptConfServiceImpl extends ServiceImpl<PssRptConfDao, PssRptConfEntity> implements PssRptConfService {
    @Autowired
    private PssRschConfService pssRschConfService;
    @Autowired
    private ScheduleJobService scheduleJobService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper qr=new QueryWrapper<PssRptConfEntity>();
        addQueryCondition(params,"rptType","rpt_type",qr);
        addQueryCondition(params,"rptFreq","rpt_Freq",qr);
        addQueryCondition(params,"rcshId","rcsh_id",qr);
        qr.orderByDesc("crte_date");

        IPage<PssRptConfEntity> page = this.page(
                new Query<PssRptConfEntity>().getPage(params),
                qr
        );

        return new PageUtils(page);
    }

    private void addQueryCondition(Map<String, Object> params,String con,String con_cloum ,QueryWrapper qr){
        Object o=params.get(con);
        if(o!=null&& StringUtils.isNotBlank(o.toString())){
            qr.eq(con_cloum,o.toString());
        }
    }


    @Override
    public PssRptConfDto saveOrUpdate(PssRptConfDto dto) {
        PssRptConfEntity entity = PssRptConfEntity.toEntity(dto);
        super.saveOrUpdate(entity);
        if ("1".equals(entity.getRptType())) return PssRptConfEntity.toData(entity);


        //根据调度配置id获取配置信息
        PssRschConfEntity pe= pssRschConfService.getById(entity.getRschId());

        //根据调度配置建立定时任务
        ScheduleJobEntity sb=new ScheduleJobEntity();
         //根据调度配置频率设置 job中的cron表达式
        // :D, 周：W,旬：TD， 月频:M,  季频:Q,  年频:Y，指定日期:SP
        String cron="";
        String mi="01",hh="01";//1点1分钟0秒开始执行
        if(pe.getExecTime()!=null){//没有指定执行时间
             hh=new DateTime(pe.getExecTime()).getHour()+"";
             mi=new DateTime(pe.getExecTime()).getMinute()+"";
          }
        String smh="0 "+(mi.equals("")?"01":mi)+" "+(hh.equals("")?"01":hh);
        if ("D".equals(pe.getRschFreq())){
            cron=smh+" * * ? *";//一号开始每天执行一次
        }else if ("W".equals(pe.getRschFreq())){
             cron=smh+" ? * MON";  //每周一 HH点MI分钟0秒开始执行
        }else if ("TD".equals(pe.getRschFreq())){
            cron=smh+" 1/10 * ? *";//一号开始每10天执行一次
        }else if ("M".equals(pe.getRschFreq())){
            cron=smh+" 1 1/1 ?";// 从  1 日开始,每 1 月执行一次
        }
        else if ("Y".equals(pe.getRschFreq())){
            cron=smh+" 1 1 ? *";// 从  1 日开始,每 1 月执行一次
        }
        sb.setCronExpression(cron);
        sb.setExecTime(pe.getExecTime());
        sb.setExecType(pe.getExecType());
        sb.setStatus(0);
        sb.setRemark(pe.getRschRemark());
        sb.setCreateTime(new Date());
        sb.setParams(entity.getRptId()+"");
        sb.setBeanName("testTask2");
        scheduleJobService.saveJob(sb);

        //保存完成后启动任务
        scheduleJobService.run(new Long[]{sb.getJobId()});
        return PssRptConfEntity.toData(entity);
    }

}