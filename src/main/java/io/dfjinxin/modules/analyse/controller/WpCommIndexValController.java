package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpCommIndexValService;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 价格监测子系统-价格分析-趋势分析
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@RestController
@RequestMapping("analyse/trend")
@Api(tags = "WpCommIndexValController", description = "价格分析-趋势分析")
public class WpCommIndexValController {
    @Autowired
    private WpCommIndexValService wpCommIndexValService;

    /**
     * 列表
     */
    @GetMapping("/queryList")
    @ApiOperation("趋势分析-趋势分析页")
    public R queryList() {

        List<Map<String, PssCommTotalEntity>> list = wpCommIndexValService.queryList();
        return R.ok().put("data", list);
    }

    /**
     * 根据 商品id 获取相应 指标类型
     */
    @GetMapping("/detail/queryIndexType/{commId}")
    @ApiOperation("趋势分析详情页-根据 商品id 获取相应 指标类型")
    public R queryIndexTypeByCommId(@PathVariable("commId") Integer commId) {

        List<Map<String, Object>> map  = wpCommIndexValService.queryIndexTypeByCommId(commId);
        return R.ok().put("data", map);
    }
    /**
     * 显示统计详情
     */
    @PostMapping("/detail/")
    @ApiOperation("趋势分析详情页 根据  指标类型，商品id，日期（默认最近一个月）  获取相应统计详情 ")
    public R queryDetailByCondition(@RequestParam(value = "commId", required = false) Integer commId,
                                 @RequestParam(value = "indexType", required = true) String indexType,
                                 @RequestParam(value = "dateFrom", required = false) String dateFrom,
                                 @RequestParam(value = "dateTo", required = false) String dateTo
                                 ) {
        if(StringUtils.isBlank(dateFrom)){
            if( StringUtils.isBlank(dateTo)){
                dateFrom=new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDateMonths(new Date(),-1));
                dateTo=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }else{
                dateFrom="";
            }

        }
        Map<String, Object> params = new HashMap<String, Object>() { };
        params.put("commId", commId); params.put("indexType", indexType);
        params. put("dateTo", dateTo); params.put("dateFrom", dateFrom);

        List<Map<String, Object>> map = wpCommIndexValService.queryDetailByCommId(params);
        return R.ok().put("data", map);
    }

}
