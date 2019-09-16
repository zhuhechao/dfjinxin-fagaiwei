package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpMcroIndexInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@RestController
@RequestMapping("analyse/macro")
@Api(tags = "WpMcroIndexInfoController", description = "价格分析-宏观分析")
public class WpMcroIndexInfoController {

    @Autowired
    private WpMcroIndexInfoService wpMcroIndexInfoService;


    /**
     * 列表
     */
    @GetMapping("/query")
    @ApiOperation("宏观分析-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexName", value = "指标名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaCodes", value = "统计区域集合,分割", required = false, dataType = "String", paramType = "query")
    })
    public R query(@RequestParam(name = "indexName", required = false) String indexName,
                   @RequestParam(name = "dateFrom", required = false) String dateFrom,
                   @RequestParam(name = "dateTo", required = false) String dateTo,
                   @RequestParam(name = "areaCodes", required = false) String areaCodes
    ) {
        PageUtils page = wpMcroIndexInfoService.queryByPage(indexName,dateFrom,dateTo,areaCodes);
//        return R.ok().put("tableData", page).put("echartsData", page);
        return R.ok().put("data", page);
    }

    @GetMapping("getName")
    @ApiOperation("宏观分析-获取指标名称")
    public R getName() {
        List<WpMcroIndexInfoEntity> wpMcroIndexInfos = wpMcroIndexInfoService.getName();
        return R.ok().put("data", wpMcroIndexInfos);
    }

}
