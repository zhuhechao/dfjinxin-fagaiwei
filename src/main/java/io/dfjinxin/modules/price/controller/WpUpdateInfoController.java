package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.service.WpUpdateInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用于统计每日(价格基础取值表&宏观基础取值表)数据总量
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2020-01-10 15:52:24
 */
@RestController
@RequestMapping("wp/info")
@Api(tags = "统计每日数据总量")
public class WpUpdateInfoController {
    @Autowired
    private WpUpdateInfoService wpUpdateInfoService;

    /**
     * 列表
     */
    @ApiOperation(value = "用于统计每日(价格基础取值表&宏观基础取值表)数据总量")
    @GetMapping("/getEverydayInfoTotal")
    public R getEverydayInfoTotal() {
        Long infoTotal = wpUpdateInfoService.getEverydayInfoTotal();

        return R.ok().put("data", infoTotal);
    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    @RequiresPermissions("analyse:wpupdateinfo:info")
//    public R info(@PathVariable("id") Integer id) {
//        WpUpdateInfoEntity wpUpdateInfo = wpUpdateInfoService.getById(id);
//
//        return R.ok().put("wpUpdateInfo", wpUpdateInfo);
//    }

    /* *//**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("analyse:wpupdateinfo:save")
    public R save(@RequestBody WpUpdateInfoEntity wpUpdateInfo) {
        wpUpdateInfoService.save(wpUpdateInfo);

        return R.ok();
    }

    *//**
     * 修改
     *//*
    @RequestMapping("/update")
    @RequiresPermissions("analyse:wpupdateinfo:update")
    public R update(@RequestBody WpUpdateInfoEntity wpUpdateInfo) {
        wpUpdateInfoService.updateById(wpUpdateInfo);

        return R.ok();
    }

    *//**
     * 删除
     *//*
    @RequestMapping("/delete")
    @RequiresPermissions("analyse:wpupdateinfo:delete")
    public R delete(@RequestBody Integer[] ids) {
        wpUpdateInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }*/

}
