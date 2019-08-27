package io.dfjinxin.modules.price.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;


/**
 * 价格预警结果页Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 17:21:32
 */
@RestController
@RequestMapping("price/pricee/warn")
@Api(tags = "PssPriceEwarnController", description = "价格预警结果页")
public class PssPriceEwarnController {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;

    /**
     * 列表
     */
    @GetMapping("/queryList")
    @ApiOperation("价格预警结果")
    public R queryList() {
        List<PssPriceEwarnEntity> list = pssPriceEwarnService.queryList();
        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @GetMapping("/detail/{commId}")
    @ApiOperation("价格预警详情")
    public R detail(@PathVariable("commId") Integer commId) {
        List<PssPriceEwarnEntity> list = pssPriceEwarnService.queryDetail(commId);
        List<BigDecimal> dataPoints = new ArrayList<>();
        for (PssPriceEwarnEntity entity : list) {
            dataPoints.add(entity.getPriValue());
        }
        return R.ok().put("data", dataPoints);
    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{ewarnId}")
//    @RequiresPermissions("price:psspriceewarn:info")
//    public R info(@PathVariable("ewarnId") String ewarnId){
//		PssPriceEwarnEntity pssPriceEwarn = pssPriceEwarnService.getById(ewarnId);
//
//        return R.ok().put("pssPriceEwarn", pssPriceEwarn);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("price:psspriceewarn:save")
//    public R save(@RequestBody PssPriceEwarnEntity pssPriceEwarn){
//		pssPriceEwarnService.save(pssPriceEwarn);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("price:psspriceewarn:update")
//    public R update(@RequestBody PssPriceEwarnEntity pssPriceEwarn){
//		pssPriceEwarnService.updateById(pssPriceEwarn);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("price:psspriceewarn:delete")
//    public R delete(@RequestBody String[] ewarnIds){
//		pssPriceEwarnService.removeByIds(Arrays.asList(ewarnIds));
//
//        return R.ok();
//    }

}
