package io.dfjinxin.modules.report.controller;

import java.util.*;

import io.dfjinxin.modules.report.dto.PssRptConfDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 *
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-18 11:39:58
 */
@RestController
@RequestMapping("report/pssrptconf")
@Api(tags = "调度报告配置")
public class PssRptConfController {
    @Autowired
    private PssRptConfService pssRptConfService;

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiOperation(value = "报告配置-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据集", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "rptType", value = "报告类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "rptFreq", value = "报告频度", required = false, dataType = "String", paramType = "query"),
    })
    public R list(
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "rptType", required = false) String rptType,
            @RequestParam(value = "rptFreq", required = false) String rptFreq
    ) {
        Map<String, Object> params = new HashMap() {{
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);

            put("rptType", rptType);
            put("rptFreq", rptFreq);
        }};
        PageUtils pageOne = pssRptConfService.queryPage(params);
        return R.ok().put("page", pageOne);
    }



    /**
     * 信息
     */
    @GetMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptinfo:info")
    @ApiOperation("报告运行信息获取")
    public R info(@PathVariable("rptId") Long rptId) {
        PssRptConfEntity pssRptConf = pssRptConfService.getById(rptId);

        return R.ok().put("pssRptConf", pssRptConf);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("price:pssrptconf:save")
    @ApiOperation("保存-报告配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dto", value = "保存信息", required = false, dataType = "PssRptConfDto", paramType = "query")
    })
    public R save(@RequestBody PssRptConfDto dto) {
        pssRptConfService.saveOrUpdate(dto);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("report:pssrptconf:update")
    @ApiOperation("保存-报告配置更新")
    public R update(@RequestBody PssRptConfEntity pssRptConf){
		pssRptConfService.updateById(pssRptConf);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("report:pssrptconf:delete")
    @ApiOperation("保存-报告配置删除")
    public R delete(@RequestBody Long[] rptIds){
        List rschids=Arrays.asList(rptIds);
        for (int i=0;i<rschids.size();i++){
            PssRptConfEntity pc=pssRptConfService.getById(rschids.get(i)+"");
            pc.setStatCode("1");
            pssRptConfService.updateById(pc);
        }
        return R.ok();
    }


}
