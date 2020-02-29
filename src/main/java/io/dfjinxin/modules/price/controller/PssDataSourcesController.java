package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssDataSourcesEntity;
import io.dfjinxin.modules.price.service.PssDataSourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DataSourcesController
 * @Author：lym 863968235@qq.com
 * @Date： 2019/9/19 16:45
 * 修改备注：
 */
@RestController
@RequestMapping("dataAccess/dataSources")
@Api(tags = "数据源管理-操作")
public class PssDataSourcesController {

    @Autowired
    private PssDataSourcesService dataSourcesService;


    /**
     * 分页
     */
    @GetMapping("/queryDataSourcesList")
    @ApiOperation(value = "数据源管理-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据集", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "dataName", value = "数据源名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dataType", value = "数据源类型", required = false, dataType = "Integer", paramType = "query"),
    })
    public R queryDataSourcesList(
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "dataName", required = false) String dataName,
            @RequestParam(value = "dataType", required = false) Integer dataType
    ) {
        Map<String, Object> params = new HashMap();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        params.put("dataName", dataName);
        params.put("dataType", dataType);
        PageUtils pageOne = dataSourcesService.queryPage(params);
        return R.ok().put("page", pageOne);
    }


    /**
     * 批量删除
     */
    @PostMapping("/deleteDataSources")
    @ApiOperation(value = "数据源管理-删除")
    public R deleteDataSources(@RequestBody Integer[] dataId) {
        dataSourcesService.removeByIds(Arrays.asList(dataId));
        return R.ok();
    }


    /**
     * 信息
     */
    @GetMapping("/dataSourcesInfo/{dataId}")
    public R dataSourcesInfo(@PathVariable("dataId") Integer dataId) {
        PssDataSourcesEntity dataSourcesEntity = dataSourcesService.getById(dataId);
        return R.ok().put("dataSourcesEntity", dataSourcesEntity);
    }


    /**
     * 修改
     */
    @PostMapping("/updateDataSources")
    @ApiOperation(value = "数据源管理-修改")
    public R updateDataSources(@RequestBody PssDataSourcesEntity dataSourcesEntity) {
        dataSourcesService.updateById(dataSourcesEntity);

        return R.ok();
    }


    /**
     * 保存
     */
    @PostMapping("/saveDataSources")
    @ApiOperation(value = "数据源管理-增加")
    public R saveDataSources(@RequestBody PssDataSourcesEntity dataSourcesEntity) {
        dataSourcesService.save(dataSourcesEntity);
        return R.ok();
    }

}
