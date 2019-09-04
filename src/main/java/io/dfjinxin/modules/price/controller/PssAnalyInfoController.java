package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyinfo")
@Api(tags = "分析信息")
public class PssAnalyInfoController {
    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssanalyinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = pssAnalyInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{analyId}")
    @RequiresPermissions("price:pssanalyinfo:info")
    public R info(@PathVariable("analyId") Integer analyId) {
        PssAnalyInfoEntity pssAnalyInfo = pssAnalyInfoService.getById(analyId);

        return R.ok().put("pssAnalyInfo", pssAnalyInfo);
    }

    /**
     * 运行
     */
    @PostMapping("/run")
    @RequiresPermissions("price:pssanalyinfo:run")
    @ApiOperation("运行")
    public R run(@RequestBody PssAnalyInfoDto dto) {
        PssDatasetInfoDto dataset = null;
        dto = pssAnalyInfoService.saveOrUpdate(dto);

        pssDatasetInfoService.saveOrUpdate(dataset);
        return R.ok();
    }

}
