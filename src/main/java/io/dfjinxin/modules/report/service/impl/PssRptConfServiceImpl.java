package io.dfjinxin.modules.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.job.entity.ScheduleJobEntity;
import io.dfjinxin.modules.job.service.ScheduleJobService;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommConfService;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.report.dao.PssRptConfDao;
import io.dfjinxin.modules.report.dao.PssRptInfoDao;
import io.dfjinxin.modules.report.dto.PssRptConfDto;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.entity.PssRptTemplateEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.modules.report.service.PssRptTemplateService;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.dfjinxin.modules.sys.service.PssRschConfService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


@Service("pssRptConfService")
public class PssRptConfServiceImpl extends ServiceImpl<PssRptConfDao, PssRptConfEntity> implements PssRptConfService {
    @Autowired
    private PssRschConfService pssRschConfService;
    @Autowired
    private PssRptInfoService pssRptInfoService;
    @Autowired
    private PssCommTotalService pssCommTotalService;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private PssRptTemplateService pssRptTemplateService;

    @Resource
    private PssRptConfDao pssRptConfDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = (Page) super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }


    @Override
    public PssRptConfDto saveOrUpdate(PssRptConfDto dto) {
        PssRptConfEntity entity = PssRptConfEntity.toEntity(dto);
        entity.setRptStatus("0");
        entity.setStatCode("中国");

        if (dto.getTempId() != null) {
            PssRptTemplateEntity rptTemplate = pssRptTemplateService.getById(dto.getTempId());
            entity.setTempId(rptTemplate.getTempId());
            entity.setRptPath(rptTemplate.getRptPath());
            super.saveOrUpdate(entity);
        } else {
            super.saveOrUpdate(entity);
        }
        if ("1".equals(entity.getRptType())) {
            PssRptInfoEntity prie = new PssRptInfoEntity();
            //保存商品信息
            PssCommTotalEntity pte = pssCommTotalService.getById(entity.getCommId());
            prie.setCommId(pte.getCommId());
            prie.setCommName(pte.getCommName());
            prie.setRptType("1");
            prie.setRptFreq(entity.getRptFreq());
            prie.setCrteTime(entity.getCrteDate());
            prie.setRptName(entity.getRptName());
            prie.setRptPath(entity.getRptAttachmentPath());
            prie.setRptStatus("1");
            prie.setRptFile("");
            pssRptInfoService.saveOrUpdate(prie);
            return PssRptConfEntity.toData(entity);
        }


        //根据调度配置id获取配置信息
        PssRschConfEntity pe = pssRschConfService.getById(entity.getRschId());

        //根据调度配置建立定时任务
        ScheduleJobEntity sb = new ScheduleJobEntity();
        //根据调度配置频率设置 job中的cron表达式
        // :D, 周：W,旬：TD， 月频:M,  季频:Q,  年频:Y，指定日期:SP
        String cron = "";
        String mi = "01", hh = "01";//1点1分钟0秒开始执行
        if (pe.getExecTime() != null) {//没有指定执行时间
            hh = new DateTime(pe.getExecTime()).getHour() + "";
            mi = new DateTime(pe.getExecTime()).getMinute() + "";
        }
        String smh = "0 " + (mi.equals("") ? "01" : mi) + " " + (hh.equals("") ? "01" : hh);
        if ("D".equals(pe.getRschFreq())) {
            cron = smh + " * * ? *";//一号开始每天执行一次
        } else if ("W".equals(pe.getRschFreq())) {
            cron = smh + " ? * MON";  //每周一 HH点MI分钟0秒开始执行
        } else if ("TD".equals(pe.getRschFreq())) {
            cron = smh + " 1/10 * ? *";//一号开始每10天执行一次
        } else if ("M".equals(pe.getRschFreq())) {
            cron = smh + " 1 1/1 ?";// 从  1 日开始,每 1 月执行一次
        } else if ("Y".equals(pe.getRschFreq())) {
            cron = smh + " 1 1 ? *";// 从  1 日开始,每 1 月执行一次
        }
        sb.setCronExpression(cron);
        sb.setExecTime(pe.getExecTime());
        sb.setExecType(pe.getExecType());
        sb.setStatus(0);
        sb.setRemark(pe.getRschRemark());
        sb.setCreateTime(new Date());
        sb.setParams(entity.getRptId() + "");
        sb.setBeanName("testTask2");
        scheduleJobService.saveJob(sb);

        //保存完成后启动任务
        scheduleJobService.run(new Long[]{sb.getJobId()});
        return PssRptConfEntity.toData(entity);
    }

}
