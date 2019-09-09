package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssRptInfoEntity;
import io.dfjinxin.modules.price.service.PssRptInfoService;
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

}
