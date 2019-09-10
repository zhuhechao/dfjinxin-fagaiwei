package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.entity.PssRschConfEntity;
import io.dfjinxin.modules.price.service.PssRschConfService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-10 09:22:42
 */
@RestController
@RequestMapping("price/pssrschconf")
public class PssRschConfController {
    @Autowired
    private PssRschConfService pssRschConfService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssrschconf:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRschConfService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{rschId}")
    @RequiresPermissions("price:pssrschconf:info")
    public R info(@PathVariable("rschId") String rschId){
        PssRschConfEntity pssRschConf = pssRschConfService.getById(rschId);

        return R.ok().put("pssRschConf", pssRschConf);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("price:pssrschconf:save")
    public R save(@RequestBody PssRschConfEntity pssRschConf){
        pssRschConfService.save(pssRschConf);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("price:pssrschconf:update")
    public R update(@RequestBody PssRschConfEntity pssRschConf){
        ValidatorUtils.validateEntity(pssRschConf);
        pssRschConfService.updateById(pssRschConf);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:pssrschconf:delete")
    public R delete(@RequestBody String[] rschIds){
        pssRschConfService.removeByIds(Arrays.asList(rschIds));

        return R.ok();
    }

}
