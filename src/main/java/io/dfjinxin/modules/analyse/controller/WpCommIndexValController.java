package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpCommIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpCommIndexValService;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 价格监测子系统-价格分析-趋势分析
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-02 15:38:19
 */
@RestController
@RequestMapping("analyse/trend")
@Api(tags = "WpCommIndexValController", description = "价格分析-趋势分析")
public class WpCommIndexValController {
    @Autowired
    private WpCommIndexValService wpCommIndexValService;

    /**
     * 列表
     */
    @GetMapping("/queryList")
    @ApiOperation("趋势分析-趋势分析页")
    public R queryList() {

        List<Map<String, PssCommTotalEntity>> list = wpCommIndexValService.queryList();
        return R.ok().put("data", list);
    }


    /**
     * commId 二级商品id
     */
    @GetMapping("/detail/{commId}")
    @ApiOperation("趋势分析详情")
    public R queryDetailByCommId(@PathVariable("commId") Integer commId) {
        Map<String, Object> map = wpCommIndexValService.queryDetailByCommId(commId);
        return R.ok().put("data", map);
    }

}
