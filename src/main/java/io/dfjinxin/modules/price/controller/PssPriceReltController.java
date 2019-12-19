package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
     * @Param: [commName, foreType, parentCode, levelCode, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/18 15:25
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "价格分析-价格预测-详情", notes = "参数为:规格品id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })
    public R detail(@PathVariable("id") Long id,
                    @RequestParam(value = "dateFrom", required = false) String dateFrom,
                    @RequestParam(value = "dateTo", required = false) String dateTo) {
        Map<String, Object> map = pssPriceReltService.detail(id, dateFrom, dateTo);
        return R.ok().put("data", map);
    }


    @GetMapping("/query/comm")
    @RequiresPermissions("price:psspricerelt:querycomm")
    @ApiOperation("查询商品预测结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "商品id", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R queryComm(@RequestParam(value = "commId", required = false) Integer commId,
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
        PageUtils page = pssPriceReltService.queryPage(params);

        return R.ok().put("page", page);
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
