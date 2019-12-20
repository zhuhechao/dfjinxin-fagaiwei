package io.dfjinxin.modules.sys.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.sys.entity.PssRschConfEntity;
import io.dfjinxin.modules.sys.service.PssRschConfService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 *
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-14 16:04:09
 */
@RestController
@RequestMapping("job/pssrschconf")
@Api(tags = "调度管理-调度配置")

public class PssRschConfController {
    @Autowired
    private PssRschConfService pssRschConfService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("条件查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "params", value = "map参数", required = false, dataType = "String", paramType = "query")
    })
    //@RequiresPermissions("job:pssrschconf:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRschConfService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/queryRschList")
    @ApiOperation("所有调度列表")
    public R queryRschList() {
        List<PssRschConfEntity> page = pssRschConfService.list();

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{rschId}")
    @ApiOperation("单信息查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "rschId", value = "主键id", required = false, dataType = "Long", paramType = "query")
    })
    @RequiresPermissions("job:pssrschconf:info")
    public R info(@PathVariable("rschId") Long rschId){
		PssRschConfEntity pssRschConf = pssRschConfService.getById(rschId);

        return R.ok().put("pssRschConf", pssRschConf);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    @ApiImplicitParams({@ApiImplicitParam(name = "pssRschConf", value = "详细信息", required = true, dataType = "PssRschConfEntity", paramType = "query")
    })
    @RequiresPermissions("job:pssrschconf:save")
    public R save(@RequestBody PssRschConfEntity pssRschConf){
        pssRschConf.setDel_flag("0");
        pssRschConf.setCreateTime(new Date());
		pssRschConfService.save(pssRschConf);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pssRschConf", value = "详细信息", required = true, dataType = "PssRschConfEntity", paramType = "query")
    })
    @RequiresPermissions("job:pssrschconf:update")
    public R update(@RequestBody PssRschConfEntity pssRschConf){
        pssRschConfService.updateById(pssRschConf);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pssRschConf", value = "主键id数组", required = true, dataType = "long[]", paramType = "query")
    })
    @RequiresPermissions("job:pssrschconf:delete")
    public R delete(@RequestBody Long[] rschIds){
        List rschids=Arrays.asList(rschIds);
        for (int i=0;i<rschids.size();i++){
            PssRschConfEntity pc=pssRschConfService.getById(rschids.get(i)+"");
            pc.setDel_flag("1");
            pssRschConfService.updateById(pc);
        }

        return R.ok();
    }

}
