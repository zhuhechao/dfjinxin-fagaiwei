package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssForeModResultEntity;
import io.dfjinxin.modules.price.service.PssForeModResultService;
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
@RequestMapping("price/pssforemodresult")
public class PssForeModResultController {
    @Autowired
    private PssForeModResultService pssForeModResultService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssforemodresult:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssForeModResultService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{modId}")
    @RequiresPermissions("price:pssforemodresult:info")
    public R info(@PathVariable("modId") String modId){
        PssForeModResultEntity pssForeModResult = pssForeModResultService.getById(modId);

        return R.ok().put("pssForeModResult", pssForeModResult);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:pssforemodresult:save")
    public R save(@RequestBody PssForeModResultEntity pssForeModResult){
        pssForeModResultService.save(pssForeModResult);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @RequiresPermissions("price:pssforemodresult:update")
    public R update(@RequestBody PssForeModResultEntity pssForeModResult){
        ValidatorUtils.validateEntity(pssForeModResult);
        pssForeModResultService.updateById(pssForeModResult);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:pssforemodresult:delete")
    public R delete(@RequestBody String[] modIds){
        pssForeModResultService.removeByIds(Arrays.asList(modIds));

        return R.ok();
    }

}
