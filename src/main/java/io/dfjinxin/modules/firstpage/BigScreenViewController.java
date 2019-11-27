package io.dfjinxin.modules.firstpage;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Desc: 大屏-首页
 * @Author: z.h.c
 * @Date: 2019/11/21 15:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("bigScreen/firstPage")
@Api(tags = "大屏-首页")
public class BigScreenViewController {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;

    /**
    * @Desc: 商品预警信息
    * @Param: []
    * @Return: io.dfjinxin.common.utils.R
    * @Author: z.h.c
    * @Date: 2019/11/27 10:18
    */
    @GetMapping("/commEwarn")
    @ApiOperation("大屏-首页-商品预警信息")
    public R bg_firstPage_commEwarn() {
        Map<String,Object> map = pssPriceEwarnService.bg_firstPage_commEwarn();

        return R.ok().put("data", map);
    }

    @GetMapping("/riskInfo")
    @ApiOperation("大屏-首页-风险信息")
    public R bg_firstPage_riskInfo() {
        Map<String,Object> map = pssPriceEwarnService.bg_firstPage_riskInfo();

        return R.ok().put("data", map);
    }

}
