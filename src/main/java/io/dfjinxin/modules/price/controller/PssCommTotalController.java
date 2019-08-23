package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;


/**
 * 价格监测子系统-价格预警-价格预警配置-商品配置Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-23 15:38:58
 */
@RestController
@RequestMapping("price/comm/conf")
@Api(tags = "PssCommTotalController", description = "价格监测子系统-商品配置")
public class PssCommTotalController {
    @Autowired
    private PssCommTotalService pssCommTotalService;

    @GetMapping("/getCommType")
    @ApiOperation("商品配置-获取商品类型&商品大类")
    public R getCommType() {
        Map<String, List<PssCommTotalEntity>> result = pssCommTotalService.queryCommType();
        return R.ok().put("data", result);
    }


//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = pssCommTotalService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{commId}")
//    @RequiresPermissions("price:psscommtotal:info")
//    public R info(@PathVariable("commId") String commId) {
//        PssCommTotalEntity pssCommTotal = pssCommTotalService.getById(commId);
//
//        return R.ok().put("pssCommTotal", pssCommTotal);
//    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("price:psscommtotal:save")
//    public R save(@RequestBody PssCommTotalEntity pssCommTotal) {
//        pssCommTotalService.save(pssCommTotal);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("price:psscommtotal:update")
//    public R update(@RequestBody PssCommTotalEntity pssCommTotal) {
//        pssCommTotalService.updateById(pssCommTotal);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("price:psscommtotal:delete")
//    public R delete(@RequestBody String[] commIds) {
//        pssCommTotalService.removeByIds(Arrays.asList(commIds));
//
//        return R.ok();
//    }

}
