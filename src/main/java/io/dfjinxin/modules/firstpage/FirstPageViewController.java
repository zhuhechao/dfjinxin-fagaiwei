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
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/9/28 11:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("price/first")
@Api(tags = "First page-首页展示")
public class FirstPageViewController {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;

    /**
     * 列表
     */
    @GetMapping("/view")
    @ApiOperation("首页-商品预警详细")
    public R firstpage() {
        Map<String, Object> map = pssPriceEwarnService.firstPageView(true);

        return R.ok().put("data", map);
    }

}
