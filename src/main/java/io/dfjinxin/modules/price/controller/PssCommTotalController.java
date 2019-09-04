package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommConfService;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 价格监测子系统-价格预警-价格预警配置-商品配置Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-23 15:38:58
 */
@RestController
@RequestMapping("price/comm/conf")
@Api(tags = "PssCommTotalController", description = "价格监测子系统-商品配置")
public class PssCommTotalController {

    @Autowired
    private PssCommTotalService pssCommTotalService;

    @Autowired
    private PssCommConfService pssCommConfService;

    @GetMapping("/getCommType")
    @ApiOperation("商品配置-获取商品类型&商品大类")
    public R getCommType() {
        Map<String, List<PssCommTotalEntity>> result = pssCommTotalService.queryCommType();
        return R.ok().put("data", result);
    }

    @PostMapping("/queryPageList")
    @ApiOperation("商品配置-查询")
    public R queryPageList(@RequestBody PssCommTotalDto params) {

        if (params == null) {
            R.error("请求参数为空!");
        }
        params.startNumber();
        PageUtils page = pssCommTotalService.queryPageList(params);
        return R.ok().put("data", page);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("商品配置-保存配置")
    public R save(@RequestBody Map<String, List<Integer>> params) {
        if (params.isEmpty()) {
            R.error("请求参数为空!");
        }
        pssCommConfService.saveCommConf(params);
        return R.ok();
    }

}