package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.dto.PssRschConfDto;
import io.dfjinxin.modules.price.entity.PssRschConfEntity;
import io.dfjinxin.modules.price.service.PssRschConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "调度配置")
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
    @ApiOperation("保存")
    public R save(@RequestBody PssRschConfDto dto){
        dto = pssRschConfService.saveOrUpdate(dto);

        return R.ok().put("data", dto);
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
