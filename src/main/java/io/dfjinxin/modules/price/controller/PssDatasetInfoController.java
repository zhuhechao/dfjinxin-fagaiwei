package io.dfjinxin.modules.price.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.dfjinxin.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@RestController
@RequestMapping("price/pssdatasetinfo")
@Api(tags = "PssDatasetInfoController", description = "数据集信息")
public class PssDatasetInfoController {
    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;

    /**
     * 列表
     */
    @GetMapping("/listAll")
    @RequiresPermissions("price:pssdatasetinfo:list")
    @ApiOperation("返回所有数据集")
    public R listAll(){
        List<PssDatasetInfoEntity> list = pssDatasetInfoService.listAll();

        return R.ok().put("list", list);
    }
}
