package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.service.WpCommPriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-27 17:23:11
 */
@RestController
@RequestMapping("price/ewarn/result")
public class WpCommPriController {
    @Autowired
    private WpCommPriService wpCommPriService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:wpcommpri:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wpCommPriService.queryPage(params);

        return R.ok().put("page", page);
    }


//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    @RequiresPermissions("price:wpcommpri:info")
//    public R info(@PathVariable("id") Integer id){
//		WpCommPriEntity wpCommPri = wpCommPriService.getById(id);
//
//        return R.ok().put("wpCommPri", wpCommPri);
//    }


}
