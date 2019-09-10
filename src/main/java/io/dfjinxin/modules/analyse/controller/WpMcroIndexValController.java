package io.dfjinxin.modules.analyse.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.common.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjinxin.modules.analyse.entity.WpMcroIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpMcroIndexValService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-09 11:23:36
 */
@RestController
@RequestMapping("analyse/macro/info")
public class WpMcroIndexValController {
    @Autowired
    private WpMcroIndexValService wpMcroIndexValService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("analyse:wpmcroindexval:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wpMcroIndexValService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{seqId}")
    @RequiresPermissions("analyse:wpmcroindexval:info")
    public R info(@PathVariable("seqId") Integer seqId){
		WpMcroIndexValEntity wpMcroIndexVal = wpMcroIndexValService.getById(seqId);

        return R.ok().put("wpMcroIndexVal", wpMcroIndexVal);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("analyse:wpmcroindexval:save")
    public R save(@RequestBody WpMcroIndexValEntity wpMcroIndexVal){
		wpMcroIndexValService.save(wpMcroIndexVal);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("analyse:wpmcroindexval:update")
    public R update(@RequestBody WpMcroIndexValEntity wpMcroIndexVal){
		wpMcroIndexValService.updateById(wpMcroIndexVal);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("analyse:wpmcroindexval:delete")
    public R delete(@RequestBody Integer[] seqIds){
		wpMcroIndexValService.removeByIds(Arrays.asList(seqIds));

        return R.ok();
    }

}
