package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyinfo")
@Api(tags = "PssAnalyInfoController", description = "分析信息")
public class PssAnalyInfoController {
    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("price:pssanalyinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssAnalyInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{analyId}")
    @RequiresPermissions("price:pssanalyinfo:info")
    public R info(@PathVariable("analyId") Integer analyId){
        PssAnalyInfoEntity pssAnalyInfo = pssAnalyInfoService.getById(analyId);

        return R.ok().put("pssAnalyInfo", pssAnalyInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("price:pssanalyinfo:save")
    public R save(@RequestBody PssAnalyInfoEntity pssAnalyInfo){
        pssAnalyInfoService.save(pssAnalyInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("price:pssanalyinfo:update")
    public R update(@RequestBody PssAnalyInfoEntity pssAnalyInfo){
        ValidatorUtils.validateEntity(pssAnalyInfo);
        pssAnalyInfoService.updateById(pssAnalyInfo);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("price:pssanalyinfo:delete")
    public R delete(@RequestBody Integer[] analyIds){
        pssAnalyInfoService.removeByIds(Arrays.asList(analyIds));

        return R.ok();
    }

}
