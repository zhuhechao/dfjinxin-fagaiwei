package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import io.dfjinxin.modules.price.service.PssIndexReltService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 17:32:05
 */
@RestController
@RequestMapping("price/pssindexrelt")
public class PssIndexReltController {
    @Autowired
    private PssIndexReltService pssIndexReltService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssindexrelt:list")
    public R list(@RequestParam(name = "indexName") String indexName,
                  @RequestParam(name = "dateFrom") Date dateFrom,
                  @RequestParam(name = "dateTo") Date dateTo){
        PageUtils page = pssIndexReltService.queryPage(null);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{indexId}")
    @RequiresPermissions("price:pssindexrelt:info")
    public R info(@PathVariable("indexId") String indexId){
        PssIndexReltEntity pssIndexRelt = pssIndexReltService.getById(indexId);

        return R.ok().put("pssIndexRelt", pssIndexRelt);
    }

    /**
     * 保存
     */
    @GetMapping("/save")
    @RequiresPermissions("price:pssindexrelt:save")
    public R save(@RequestBody PssIndexReltEntity pssIndexRelt){
        pssIndexReltService.save(pssIndexRelt);

        return R.ok();
    }

    /**
     * 修改
     */
    @GetMapping("/update")
    @RequiresPermissions("price:pssindexrelt:update")
    public R update(@RequestBody PssIndexReltEntity pssIndexRelt){
        ValidatorUtils.validateEntity(pssIndexRelt);
        pssIndexReltService.updateById(pssIndexRelt);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("price:pssindexrelt:delete")
    public R delete(@RequestBody String[] indexIds){
        pssIndexReltService.removeByIds(Arrays.asList(indexIds));

        return R.ok();
    }

}
