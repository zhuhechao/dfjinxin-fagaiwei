package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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
        Map<String,Object> map = pssPriceEwarnService.queryList();
        return R.ok().put("data", map);
    }

    /**
     * 列表
     */
    @GetMapping("/detail/{commId}")
    @ApiOperation("价格预警详情")
    public R detail(@PathVariable("commId") Integer commId, @RequestParam Integer ewarnTypeId) {
        List<Object> list = pssPriceEwarnService.queryDetail(commId, ewarnTypeId);
        return R.ok().put("data", list);
    }

}
