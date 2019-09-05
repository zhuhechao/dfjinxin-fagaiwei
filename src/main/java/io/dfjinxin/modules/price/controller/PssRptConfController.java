package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssRptConfEntity;
import io.dfjinxin.modules.price.service.PssRptConfService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:13:08
 */
@RestController
@RequestMapping("price/pssrptconf")
public class PssRptConfController {
    @Autowired
    private PssRptConfService pssRptConfService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssrptconf:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRptConfService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{commId}")
    @RequiresPermissions("price:pssrptconf:info")
    public R info(@PathVariable("commId") String commId){
        PssRptConfEntity pssRptConf = pssRptConfService.getById(commId);

        return R.ok().put("pssRptConf", pssRptConf);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:pssrptconf:save")
    public R save(@RequestBody PssRptConfEntity pssRptConf){
        pssRptConfService.save(pssRptConf);

        return R.ok();
    }
}
