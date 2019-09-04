package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
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
    public R query(@RequestParam(value = "commName", required = false) String commName,
                   @RequestParam(value = "modType", required = false) String modType,
                   @RequestParam(value = "parentCode", required = false) Integer parentCode,
                   @RequestParam(value = "levelCode", required = false) Integer levelCode,
                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap() {{
            put("commName", commName);
            put("modType", modType);

            put("parentCode", parentCode);
            put("levelCode", levelCode);

            put("start", (pageIndex - 1) * pageSize);
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
