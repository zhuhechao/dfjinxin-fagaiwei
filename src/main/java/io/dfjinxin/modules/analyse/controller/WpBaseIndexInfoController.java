package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:20
 */
@RestController
@RequestMapping("price/wpbaseindexinfo")
public class WpBaseIndexInfoController {
    @Autowired
    private WpBaseIndexInfoService wpBaseIndexInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("price:wpbaseindexinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wpBaseIndexInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{indexId}")
    @RequiresPermissions("price:wpbaseindexinfo:info")
    public R info(@PathVariable("indexId") Integer indexId){
		WpBaseIndexInfoEntity wpBaseIndexInfo = wpBaseIndexInfoService.getById(indexId);

        return R.ok().put("wpBaseIndexInfo", wpBaseIndexInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("price:wpbaseindexinfo:save")
    public R save(@RequestBody WpBaseIndexInfoEntity wpBaseIndexInfo){
		wpBaseIndexInfoService.save(wpBaseIndexInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("price:wpbaseindexinfo:update")
    public R update(@RequestBody WpBaseIndexInfoEntity wpBaseIndexInfo){
		wpBaseIndexInfoService.updateById(wpBaseIndexInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("price:wpbaseindexinfo:delete")
    public R delete(@RequestBody Integer[] indexIds){
		wpBaseIndexInfoService.removeByIds(Arrays.asList(indexIds));

        return R.ok();
    }

}
