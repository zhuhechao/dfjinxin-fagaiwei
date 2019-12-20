package io.dfjinxin.modules.sys.service.impl;

import io.dfjinxin.common.utils.QuartzUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;

import io.dfjinxin.modules.sys.dao.PssRschConfDao;
import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.dfjinxin.modules.sys.service.PssRschConfService;


@Service("pssRschConfService")
public class PssRschConfServiceImpl extends ServiceImpl<PssRschConfDao, PssRschConfEntity> implements PssRschConfService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper qr = new QueryWrapper<PssRschConfEntity>();
        addQueryCondition(params, "rptType", "rpt_type", "eq", qr);
        addQueryCondition(params, "rschFreq", "rsch_Freq", "eq", qr);
        addQueryCondition(params, "rschName", "rsch_name", "like", qr);
        params.put("delFlag", "0");
        addQueryCondition(params, "delFlag", "del_flag", "eq", qr);
        //addQueryCondition(params,"rcshId","rcsh_id",qr);
        qr.orderByDesc("create_time");

        IPage<PssRschConfEntity> page = this.page(
                new Query<PssRschConfEntity>().getPage(params),
                qr
        );

        return new PageUtils(page);
    }


    private void addQueryCondition(Map<String, Object> params, String con, String con_cloum, String queryType, QueryWrapper qr) {
        Object o = params.get(con);
        if (o != null && StringUtils.isNotBlank(o.toString())) {
            if ("like".equals(queryType)) {
                qr.like(con_cloum, o.toString());
            }
            if ("eq".equals(queryType)) {
                qr.eq(con_cloum, o.toString());
            }

        }
    }


}