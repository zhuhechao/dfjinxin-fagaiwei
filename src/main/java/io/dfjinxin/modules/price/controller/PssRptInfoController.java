package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssRptInfoEntity;
import io.dfjinxin.modules.price.service.PssRptInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:22:40
 */
@RestController
@RequestMapping("price/pssrptinfo")
public class PssRptInfoController {
    @Autowired
    private PssRptInfoService pssRptInfoService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssrptinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRptInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{rptId}")
    @RequiresPermissions("price:pssrptinfo:info")
    public R info(@PathVariable("rptId") String rptId){
        PssRptInfoEntity pssRptInfo = pssRptInfoService.getById(rptId);

        return R.ok().put("pssRptInfo", pssRptInfo);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:pssrptinfo:save")
    public R save(@RequestBody PssRptInfoEntity pssRptInfo){
        pssRptInfoService.save(pssRptInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @RequiresPermissions("price:pssrptinfo:update")
    public R update(@RequestBody PssRptInfoEntity pssRptInfo){
        ValidatorUtils.validateEntity(pssRptInfo);
        pssRptInfoService.updateById(pssRptInfo);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:pssrptinfo:delete")
    public R delete(@RequestBody String[] rptIds){
        pssRptInfoService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }

}
