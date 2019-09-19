package io.dfjinxin.modules.report.controller;

import java.util.*;

import io.dfjinxin.modules.price.dto.PssRptInfoDto;
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
@Api(tags = "PssRptConfController", description = "分析报告-调度报告配置")
public class PssRptConfController {
    @Autowired
    private PssRptConfService pssRptConfService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("report:pssrptconf:list")
    @ApiOperation("报告配置列表分页")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = pssRptConfService.queryPage(params);

        return R.ok().put("page", page);
    }




    /**
     * 信息
     */
    @RequestMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptconf:info")
    @ApiOperation("报告配置信息查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "rptId", value = "主键id", required = true, dataType = "Long", paramType = "query")
    })
    public R info(@PathVariable("rptId") Long rptId){
		PssRptConfEntity pssRptConf = pssRptConfService.getById(rptId);

        return R.ok().put("pssRptConf", pssRptConf);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("price:pssrptconf:save")
    @ApiOperation("保存 报告配置信息")
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
    @RequestMapping("/update")
    @RequiresPermissions("report:pssrptconf:update")
    @ApiOperation("保存 报告配置更新")
    public R update(@RequestBody PssRptConfEntity pssRptConf){
		pssRptConfService.updateById(pssRptConf);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:pssrptconf:delete")
    @ApiOperation("保存 报告配置删除")
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
