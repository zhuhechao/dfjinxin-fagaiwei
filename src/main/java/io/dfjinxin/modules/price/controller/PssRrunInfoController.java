package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjinxin.modules.price.entity.PssRrunInfoEntity;
import io.dfjinxin.modules.price.service.PssRrunInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 10:02:41
 */
@RestController
@RequestMapping("price/pssrruninfo")
public class PssRrunInfoController {
    @Autowired
    private PssRrunInfoService pssRrunInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("price:pssrruninfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRrunInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rptId}")
    @RequiresPermissions("price:pssrruninfo:info")
    public R info(@PathVariable("rptId") String rptId){
        PssRrunInfoEntity pssRrunInfo = pssRrunInfoService.getById(rptId);

        return R.ok().put("pssRrunInfo", pssRrunInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("price:pssrruninfo:save")
    public R save(@RequestBody PssRrunInfoEntity pssRrunInfo){
        pssRrunInfoService.save(pssRrunInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("price:pssrruninfo:update")
    public R update(@RequestBody PssRrunInfoEntity pssRrunInfo){
        ValidatorUtils.validateEntity(pssRrunInfo);
        pssRrunInfoService.updateById(pssRrunInfo);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("price:pssrruninfo:delete")
    public R delete(@RequestBody String[] rptIds){
        pssRrunInfoService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }

}
