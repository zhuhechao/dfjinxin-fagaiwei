package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:20
 */
@RestController
@RequestMapping("price/wpbaseindexinfo")
@Api(tags = "商品指标基础信息")
public class WpBaseIndexInfoController {
    @Autowired
    private WpBaseIndexInfoService wpBaseIndexInfoService;

    /**
     * 根据商品id获取该商品指标类型为'价格'的指标名称
     */
    @GetMapping("/info/{commId}")
    @ApiOperation("根据4类商品id获取该商品指标类型为'价格'的指标名称,eg:172,115,201")
    public R info(@PathVariable("commId") Integer commId) {
        List<WpBaseIndexInfoEntity> indexNameByList = wpBaseIndexInfoService.getIndexNameByType(commId);
        return R.ok().put("data", indexNameByList);
    }

    @GetMapping("/tree/{commId}")
    @ApiOperation("根据4类商品id获取该商品的指标类型、指标名称tree")
    public R tree(@PathVariable("commId") Integer commId) {
        List<WpBaseIndexInfoEntity> tree = wpBaseIndexInfoService.getIndexTreeByCommId(commId);
        return R.ok().put("data", tree);
    }
}
