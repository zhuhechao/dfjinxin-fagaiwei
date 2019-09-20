package io.dfjinxin.modules.report.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@RestController
@RequestMapping("report/pssrptinfo")
@Api(tags = "PssRptInfoController", description = "报告信息表")

public class PssRptInfoController {
    @Autowired
    private PssRptInfoService pssRptInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:pssrptinfo:list")
    @ApiOperation("报告配置列表分页")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRptInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptinfo:info")
    public R info(@PathVariable("rptId") Long rptId){
		PssRptInfoEntity pssRptInfo = pssRptInfoService.getById(rptId);

        return R.ok().put("pssRptInfo", pssRptInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:pssrptinfo:save")
    public R save(@RequestBody PssRptInfoEntity pssRptInfo){
		pssRptInfoService.save(pssRptInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:pssrptinfo:update")
    public R update(@RequestBody PssRptInfoEntity pssRptInfo){
		pssRptInfoService.updateById(pssRptInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:pssrptinfo:delete")
    public R delete(@RequestBody Long[] rptIds){
		pssRptInfoService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }

}
