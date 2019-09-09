package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.dto.PssRptConfDto;
import io.dfjinxin.modules.price.dto.PssRptInfoDto;
import io.dfjinxin.modules.price.entity.PssRptConfEntity;
import io.dfjinxin.modules.price.service.PssRptConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:13:08
 */
@RestController
@RequestMapping("price/pssrptconf")
@Api(tags = "报告配置")
public class PssRptConfController {
    @Autowired
    private PssRptConfService pssRptConfService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssrptconf:list")
    @ApiOperation("报告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rptType", value = "报告类型", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "rptFreq", value = "报告频度", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "rptDate", value = "报告日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "templateName", value = "模板文件", required = false, dataType = "String", paramType = "query")
    })
    public R list(@RequestParam(value = "rptType", required = false) Integer rptType,
                  @RequestParam(value = "rptFreq", required = false) Integer rptFreq,
                  @RequestParam(value = "rptDate", required = false) Date rptDate,
                  @RequestParam(value = "templateName", required = false) String templateName) {
        Map<String, Object> param = new HashMap() {{
            put("rptType", rptType);
            put("rptFreq", rptFreq);
            put("templateName", templateName);
            put("rptDate", rptDate);
        }};
        List<PssRptInfoDto> list = pssRptConfService.list(param);

        return R.ok().put("list", list);
    }

    @GetMapping("/list/template")
    @ApiOperation("模板列表")
    public R lisTtemplet() {
        return R.ok().put("list", pssRptConfService.listTemplate());
    }


    /**
     * 信息
     */
    @GetMapping("/info/{commId}")
    @RequiresPermissions("price:pssrptconf:info")
    public R info(@PathVariable("commId") String commId) {
        PssRptConfEntity pssRptConf = pssRptConfService.getById(commId);

        return R.ok().put("pssRptConf", pssRptConf);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("price:pssrptconf:save")
    public R save(@RequestBody PssRptConfDto dto) {
        pssRptConfService.saveOrUpdate(dto);

        return R.ok();
    }
}
