package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
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
import java.util.Map;


/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@RestController
@RequestMapping("analyse/macro")
@Api(tags = "价格分析-宏观分析")
public class WpMcroIndexInfoController {

    @Autowired
    private WpMcroIndexInfoService wpMcroIndexInfoService;


    /**
     * 列表
     */
    /*@GetMapping("/query")
    @ApiOperation(value = "宏观分析-查询", notes = "areaCode:0-国内，1-国外")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexId", value = "指标id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaName", value = "国家名称或国内的省份名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query")
    })
    public R query(@RequestParam(name = "indexId", required = true) String indexId,
                   @RequestParam(name = "areaName", required = true) String areaName,
                   @RequestParam(name = "dateFrom", required = false) String dateFrom,
                   @RequestParam(name = "dateTo", required = false) String dateTo
    ) {
        List<WpMcroIndexValEntity> data = wpMcroIndexInfoService.queryIndexVals(areaName, indexId, dateFrom, dateTo);
        return R.ok().put("data", data);
    }


    @GetMapping("/getIndexName")
    @ApiOperation(value = "宏观分析-获取指标名称")
    public R getAreaName() {
        List<Map<String, Object>> wpMcroIndexInfos = wpMcroIndexInfoService.getAreaName();
        return R.ok().put("data", wpMcroIndexInfos);
    }*/
    @GetMapping("/tree")
    @ApiOperation("获取宏观信息指标类型&名称tree")
    public R tree() {
        List<WpMcroIndexInfoEntity> tree = wpMcroIndexInfoService.getIndexTreeByType();
        return R.ok().put("data", tree);
    }

    /**
     * @Desc:
     * @Param: []
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/11 16:39
     */
    @GetMapping("/queryValByIndexType")
    @ApiOperation(value = "宏观分析-根据指标类型查询指标取值")
    public R macroQuery(@RequestParam("indexType") String indexType) {
        Map<String, Object> map = wpMcroIndexInfoService.queryByIndexType(indexType);
        return R.ok().put("data", map);
    }

    /**
     * @Desc:
     * @Param: []
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/11 16:39
     */
    @GetMapping("/queryIndexType")
    @ApiOperation(value = "宏观分析-查询指标类型")
    public R queryIndexType() {
        List<Map<String, Object>> data = wpMcroIndexInfoService.queryIndexType();
        return R.ok().put("data", data);
    }


}
