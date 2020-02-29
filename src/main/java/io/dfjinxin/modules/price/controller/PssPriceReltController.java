package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.dto.PssPriceReltDto;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 16:47:42
 */
@RestController
@RequestMapping("price/psspricerelt")
@Api(tags = "价格预测结果")
public class PssPriceReltController {
    @Autowired
    private PssPriceReltService pssPriceReltService;

    /**
     * @Desc: 价格分析-价格预测(根据3类商品查询该3类商品下的规格品预测信息【3类商品下最多只会有一种规格品做预测】)
     * @Param: [commName, foreType, parentCode, levelCode, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/18 15:25
     */
    @GetMapping("/query")
    @ApiOperation(value = "价格分析-价格预测(统计指定3类商品下规格品的价格预测信息)", notes = "3类商品下最多只会有一种规格品做预测")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type3CommId", value = "3类商品id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "foreType", value = "预测类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R query(
            @RequestParam(value = "foreType", required = false) String foreType,
            @RequestParam(value = "type3CommId", required = false) Integer type3CommId,
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap() {{
            put("foreType", foreType);
            put("type3CommId", type3CommId);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        PageUtils page = pssPriceReltService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * @Desc: 价格分析-价格预测-详情
     * @Param: [commId, foreType, dateFrom, dateTo]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/18 15:25
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "价格分析-价格预测-详情", notes = "参数为:规格品id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "foreType", value = "预测类型", required = true, dataType = "String", paramType = "query"),
    })
    public R detail(@PathVariable("id") Long id,
                    @RequestParam(value = "dateFrom", required = false) String dateFrom,
                    @RequestParam(value = "dateTo", required = false) String dateTo,
                    @RequestParam(value = "foreType", required = true) String foreType) {
        Map<String, Object> map = pssPriceReltService.detail(id, foreType, dateFrom, dateTo);
        return R.ok().put("data", map);
    }


    /**
     * @Desc: 二级页面(预测分析)-默认预测商品统计
     * @Param: [commId, dateFrom, dateTo, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/19 14:12
     */
    @GetMapping("/secondpage/comm")
    @ApiOperation(value = "二级页面(预测分析)(统计指定3类商品下规格品的价格预测信息)", notes = "3类商品下最多只会有一种规格品做预测")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "商品id", required = true, dataType = "Int", paramType = "query")
    })
    public R queryComm(@RequestParam(value = "commId") Integer commId) {
        PssPriceReltDto data = pssPriceReltService.queryCommByCommId(commId);
        return R.ok().put("data", data);
    }

    /**
     * @Desc: 二级页面(预测分析)-根据时间区间统计折线图&表格数据
     * @Param: [commId, dateFrom, dateTo, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/19 14:12
     */
    @GetMapping("/secondpage/linecharts")
    @ApiOperation(value = "二级页面(预测分析)根据时间区间统计折线图&表格数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "商品id", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R lineCharts(@RequestParam(value = "commId", required = true) Integer commId,
                        @RequestParam(value = "dateFrom", required = false) String dateFrom,
                        @RequestParam(value = "dateTo", required = false) String dateTo,
                        @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                        @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap() {{
            put("commId", commId);
            put("dateFrom", dateFrom);
            put("dateTo", dateTo);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        //表格数据
        PageUtils page = pssPriceReltService.getDataGrid(params);
        Map<String, Object> charts = pssPriceReltService.getLineCharts(params);
        return R.ok().put("page", page).put("lineCharts", charts);
    }

    /**
     * @Desc: 价格分析-价格预测-详情修正
     * @Param: [pssPriceRelt]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/18 16:05
     */
    @PostMapping("/update")
    @ApiOperation(value = "价格分析-价格预测-详情(修正)")
    public R update(@RequestBody PssPriceReltEntity pssPriceRelt) {
        ValidatorUtils.validateEntity(pssPriceRelt);
        pssPriceReltService.updateById(pssPriceRelt);

        return R.ok();
    }

}
