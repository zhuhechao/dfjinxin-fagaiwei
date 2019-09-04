package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssForeModEntity;
import io.dfjinxin.modules.price.service.PssForeModService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 09:33:51
 */
@RestController
@RequestMapping("price/pssforemod")
public class PssForeModController {
    @Autowired
    private PssForeModService pssForeModService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssforemod:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssForeModService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{modId}")
    @RequiresPermissions("price:pssforemod:info")
    public R info(@PathVariable("modId") Integer modId){
        PssForeModEntity pssForeMod = pssForeModService.getById(modId);

        return R.ok().put("pssForeMod", pssForeMod);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:pssforemod:save")
    public R save(@RequestBody PssForeModEntity pssForeMod){
        pssForeModService.save(pssForeMod);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @RequiresPermissions("price:pssforemod:update")
    public R update(@RequestBody PssForeModEntity pssForeMod){
        ValidatorUtils.validateEntity(pssForeMod);
        pssForeModService.updateById(pssForeMod);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:pssforemod:delete")
    public R delete(@RequestBody Integer[] modIds){
        pssForeModService.removeByIds(Arrays.asList(modIds));

        return R.ok();
    }

}
