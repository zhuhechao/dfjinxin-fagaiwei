package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssPriceReltEntity;
import io.dfjinxin.modules.price.service.PssPriceReltService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;


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
     * 查询预测结果
     */
    @GetMapping("/query")
    @RequiresPermissions("price:psspricerelt:query")
    @ApiOperation("查询预测结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commName", value = "商品名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "foreType", value = "预测类型", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "levelCode", value = "级别代码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "parentCode", value = "附级别代码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R query(@RequestParam(value = "commName", required = false) String commName,
                   @RequestParam(value = "foreType", required = false) String foreType,
                   @RequestParam(value = "parentCode", required = false) Integer parentCode,
                   @RequestParam(value = "levelCode", required = false) Integer levelCode,
                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap() {{
            put("commName", commName);
            put("foreType", foreType);

            put("parentCode", parentCode);
            put("levelCode", levelCode);

            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        PageUtils page = pssPriceReltService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/query/comm")
    @RequiresPermissions("price:psspricerelt:querycomm")
    @ApiOperation("查询商品预测结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "商品id", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R queryComm(@RequestParam(value = "commId", required = false) Integer commId,
                       @RequestParam(value = "dateFrom", required = false) Date dateFrom,
                       @RequestParam(value = "dateTo", required = false) Date dateTo,
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
     * 信息
     */
    @GetMapping("/info/{commId}")
    @RequiresPermissions("price:psspricerelt:info")
    public R info(@PathVariable("commId") String commId) {
        PssPriceReltEntity pssPriceRelt = pssPriceReltService.getById(commId);

        return R.ok().put("pssPriceRelt", pssPriceRelt);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:psspricerelt:save")
    public R save(@RequestBody PssPriceReltEntity pssPriceRelt) {
        pssPriceReltService.save(pssPriceRelt);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @RequiresPermissions("price:psspricerelt:update")
    public R update(@RequestBody PssPriceReltEntity pssPriceRelt) {
        ValidatorUtils.validateEntity(pssPriceRelt);
        pssPriceReltService.updateById(pssPriceRelt);

        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:psspricerelt:delete")
    public R delete(@RequestBody String[] commIds) {
        pssPriceReltService.removeByIds(Arrays.asList(commIds));

        return R.ok();
    }

}
