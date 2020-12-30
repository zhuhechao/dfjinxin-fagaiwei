package io.dfjinxin.modules.report.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.config.propertie.AppProperties;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.entity.WpCarwlerDataEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.modules.report.service.WpCarwlerDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@RestController
@RequestMapping("report/carwler")
@Api(tags = "报告信息-配置、管理")

public class WpCarwlerDataController {
    @Autowired
    private WpCarwlerDataService wpCarwlerDataService;


    /**
     * 信息
     */
    @GetMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptinfo:info")
    @ApiOperation("报告运行信息获取")
    public R info(@PathVariable("rptId") Long rptId) {
        WpCarwlerDataEntity pssRptInfo = wpCarwlerDataService.getById(rptId);

        return R.ok().put("pssRptInfo", pssRptInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("report:pssrptinfo:save")
    @ApiOperation("报告运行信息保存")
    public R save(@RequestBody WpCarwlerDataEntity pssRptInfo) {
        wpCarwlerDataService.save(pssRptInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("报告运行信息更新")
    public R update(@RequestBody WpCarwlerDataEntity pssRptInfo) {
        System.out.println("pssRptInfo========================="+pssRptInfo.toString());
        wpCarwlerDataService.updateById(pssRptInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
//    @RequiresPermissions("report:pssrptinfo:delete")
    @ApiOperation("报告运行信息删除")
    public R delete(@RequestBody Long[] rptIds) {
        wpCarwlerDataService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }
}
