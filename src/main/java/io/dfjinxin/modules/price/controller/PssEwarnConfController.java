package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.dto.PageListDto;
import io.dfjinxin.modules.price.entiry.PssEwarnConfEntity;
import io.dfjinxin.modules.price.service.PssEwarnConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 价格监测子系统-价格预警-价格预警配置-预警配置Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-22 14:55:38
 */

@RestController
@RequestMapping("/price/ewarn/conf")
@Api(tags = "PssEwarnConfController", description = "价格监测子系统-价格预警")
public class PssEwarnConfController {

    @Autowired
    private PssEwarnConfService pssEwarnConfService;

    @PostMapping("/queryPageList")
    @ApiOperation("预警配置-查询")
    public R list(@RequestBody PageListDto params) {

        if (params == null) {
            R.error("请求参数为空!");
        }
        params.startNumber();
        PageUtils page = pssEwarnConfService.queryPageList(params);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @PostMapping("/info/{ewarnId}")
    @ApiOperation("预警配置-查看")
    public R info(@PathVariable("ewarnId") Integer ewarnId) {
        PssEwarnConfEntity pssEwarnConf = pssEwarnConfService.getById(ewarnId);

        return R.ok().put("data", pssEwarnConf);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("预警配置-新增")
    public R save(@RequestBody PssEwarnConfEntity pssEwarnConf) {
        pssEwarnConfService.save(pssEwarnConf);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("预警配置-删除")
    public R delete(@RequestBody String ewarnId) {

        if (StringUtils.isBlank(ewarnId)) {
            return R.error("预警ID为空!");
        }
        PssEwarnConfEntity pssEwarnConfEntity = pssEwarnConfService.queryEntityByEwarnId(ewarnId);
        if (pssEwarnConfEntity == null) {
            return R.error("预警ID:" + ewarnId + ",不存在！");
        }
        pssEwarnConfEntity.setDelFlag("0");
        pssEwarnConfService.updateById(pssEwarnConfEntity);
        return R.ok();
    }

}
