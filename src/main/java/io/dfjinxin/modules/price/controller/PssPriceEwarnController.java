package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@Api(tags = "价格预警结果页")
public class PssPriceEwarnController {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;

    /**
     * @Desc:
     * @Param: []
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/13 15:42
     */
    @GetMapping("/queryList")
    @ApiOperation("价格预警结果")
    public R queryList() {
        Map<String, Object> map = pssPriceEwarnService.queryList();
        return R.ok().put("data", map);
    }

    /**
     * @Desc: 首页、预警结果页面跳转到该接口
     * 根据3类商品id,查询预警类型是【常规或非常规】的商品信息
     * @Param: [commId, ewarnTypeId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/13 15:31
     */
    @GetMapping("/indexIdConf/{commId}")
    @ApiOperation(value = "二级页面(预警展示or价格预警详情)", notes = "根据3类商品id,查询预警类型是【常规或非常规】的商品信息")
    public R queryconfByewarnTypeId(@PathVariable("commId") Integer commId, @RequestParam Integer ewarnTypeId) {
        Map<String, Object> data = pssPriceEwarnService.queryconfByewarnTypeId(commId, ewarnTypeId);
        return R.ok().put("data", data);
    }

    /**
     * @Desc: 根据指标ids, 预警类型查询 各种指标信息
     * @Param: [indexIds, ewarnTypeId, startDate, endDate]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/14 15:33
     */
    @ApiOperation("根据预警类型、指标id列表,查询指标取值")
    @GetMapping("/indexLineData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ewarnTypeId", value = "预警类型", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "indexIds", value = "指标id列表", required = true, allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })
    public R queryIndexLineData(
            @RequestParam(value = "ewarnTypeId") Integer ewarnTypeId,
            @RequestParam(value = "indexIds") Integer[] indexIds,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        Map<String, Object> data = pssPriceEwarnService.queryIndexLineData(ewarnTypeId, Arrays.asList(indexIds), startDate, endDate);
        return R.ok().put("data", data);
    }

    /**
     * @Desc: 根据预警类型、指标id，统考某类指标的月平均、年平均、当前值
     * 根据3类商品id,查询预警类型是【常规或非常规】的商品信息
     * @Param: [indexId, ewarnTypeId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/13 15:31
     */
    @GetMapping("/indexAvg")
    @ApiOperation(value = "二级页面(预警展示or价格预警详情)", notes = "根据预警类型【常规或非常规】、指标id，统考某类指标的月平均、年平均、当前值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ewarnTypeId", value = "预警类型", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "indexId", value = "指标id", required = true, dataType = "Int", paramType = "query")
    })
    public R queryIndexAvgByIndexId(
            @RequestParam Integer indexId,
            @RequestParam Integer ewarnTypeId) {
        Map<String, Object> data = pssPriceEwarnService.queryIndexAvgByIndexId(indexId, ewarnTypeId);
        return R.ok().put("data", data);
    }

}
