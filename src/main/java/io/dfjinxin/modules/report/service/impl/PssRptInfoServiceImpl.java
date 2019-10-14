package io.dfjinxin.modules.report.service.impl;

import io.dfjinxin.common.utils.DateTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.report.dao.PssRptInfoDao;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;


@Service("pssRptInfoService")
public class PssRptInfoServiceImpl extends ServiceImpl<PssRptInfoDao, PssRptInfoEntity> implements PssRptInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper qr=new QueryWrapper<PssRptInfoEntity>();
        addQueryCondition(params,"rschName","rsch_name","like",qr);
        addQueryCondition(params,"crteTime","crte_time","between",qr);
        addQueryCondition(params,"rschFreq","rpt_freq","eq",qr);
        addQueryCondition(params,"rptStatus","rpt_status","eq",qr);
        qr.orderByDesc("crteTime");

        IPage<PssRptInfoEntity> page = this.page(
                new Query<PssRptInfoEntity>().getPage(params),
                qr
        );

        return new PageUtils(page);
    }

    private void addQueryCondition(Map<String, Object> params,String con,String con_cloum ,String queryType,QueryWrapper qr){
        Object o=params.get(con);
        if(o!=null&& StringUtils.isNotBlank(o.toString())){
            if ("like".equals(queryType)){
                qr.like(con_cloum,o.toString());
            }
            if ("eq".equals(queryType)){
                qr.eq(con_cloum,o.toString());
            }
            if ("between".equals(queryType)){
                String startTime=o.toString() +"00:00:00";
                String endTime=o.toString() +"23:59:59";
                qr.between(con_cloum, DateTools.toDateByForm(startTime,"yyyy-MM-dd HH:mm:ss"),DateTools.toDateByForm(endTime,"yyyy-MM-dd HH:mm:ss"));
            }

        }
    }

}