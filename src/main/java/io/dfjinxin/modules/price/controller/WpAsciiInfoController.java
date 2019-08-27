package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;
import io.dfjinxin.modules.price.service.WpAsciiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 智慧价格码值信息表Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 14:00:37
 */
@RestController
@RequestMapping("price/ascii/info")
@Api(tags = "WpAsciiInfoController", description = "智慧价格码值信息表")
public class WpAsciiInfoController {

    @Autowired
    private WpAsciiInfoService wpAsciiInfoService;

    /**
     * 根据代码简称查询代码类型
     */
    @GetMapping("/getAsciiByType/{codeSimple}")
    @ApiOperation("根据代码简称类型,查询类型信息")
    public R getInfoByType(@PathVariable("codeSimple") String codeSimple) {

        List<WpAsciiInfoEntity> list = wpAsciiInfoService.getInfoByType(codeSimple);
        return R.ok().put("data", list);
    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{codeId}")
//    @RequiresPermissions("price:wpasciiinfo:info")
//    public R info(@PathVariable("codeId") String codeId) {
//        WpAsciiInfoEntity wpAsciiInfo = wpAsciiInfoService.getById(codeId);
//
//        return R.ok().put("wpAsciiInfo", wpAsciiInfo);
//    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("price:wpasciiinfo:save")
//    public R save(@RequestBody WpAsciiInfoEntity wpAsciiInfo){
//		wpAsciiInfoService.save(wpAsciiInfo);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("price:wpasciiinfo:update")
//    public R update(@RequestBody WpAsciiInfoEntity wpAsciiInfo){
//		wpAsciiInfoService.updateById(wpAsciiInfo);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("price:wpasciiinfo:delete")
//    public R delete(@RequestBody String[] codeIds){
//		wpAsciiInfoService.removeByIds(Arrays.asList(codeIds));
//
//        return R.ok();
//    }

}
