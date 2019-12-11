package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "价格分析-趋势分析")
public class WpBaseIndexValController {
    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    /**
     * 列表
     */
    @GetMapping("/queryList")
    @ApiOperation("趋势分析-趋势分析页")
    public R queryList() {

        List<Map<String, PssCommTotalEntity>> list = wpBaseIndexValService.queryList();
        return R.ok().put("data", list);
    }

    /**
     * 根据 商品id 获取相应 指标类型
     */
    @GetMapping("/detail/queryIndexType/{commId}")
    @ApiOperation("趋势分析详情页-根据 商品id 获取相应 指标类型")
    public R queryIndexTypeByCommId(@PathVariable("commId") Integer commId) {

        List<Map<String, Object>> map  = wpBaseIndexValService.queryIndexTypeByCommId(commId);
        return R.ok().put("data", map);
    }
    /**
     * 显示统计详情
     */
    @GetMapping("/detail/")

    @ApiOperation("趋势分析详情页 根据  指标类型，商品id，日期（默认最近一个月）  获取相应统计详情 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "商品id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "查询日期起", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "查询日期至", required = false, dataType = "String", paramType = "query")
    })
    public R queryDetailByCondition(@RequestParam(value = "commId", required = false) Integer commId,
                                 @RequestParam(value = "indexType", required = true) String indexType,
                                 @RequestParam(value = "dateFrom", required = false) String dateFrom,
                                 @RequestParam(value = "dateTo", required = false) String dateTo
                                 ) {
        if(StringUtils.isBlank(dateFrom)){
            if( StringUtils.isBlank(dateTo)){
                dateFrom=DateUtils.format(DateUtils.addDateMonths(new Date(),-1));
                dateTo=DateUtils.format(new Date());
            }else{
                dateFrom="";
            }

        }
        Map<String, Object> params = new HashMap<String, Object>() { };
        params.put("commId", commId); params.put("indexType", indexType);
        params. put("dateTo", dateTo); params.put("dateFrom", dateFrom);

        List<Map<String, Object>> map = wpBaseIndexValService.queryDetailByCommId(params);
        return R.ok().put("data", map);
    }

}
