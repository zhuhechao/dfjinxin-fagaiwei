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
 * @Date: 2019/11/21 15:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("bigScreen/")
@Api(tags = "大屏展示")
public class BigScreenViewController {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;

    /**
     * 大屏
     */
    @GetMapping("/view")
    @ApiOperation("大屏-view")
    public R bigScreenView() {
        Map<String,Object> map = pssPriceEwarnService.bigScreenView();

        return R.ok().put("data", map);
    }

}
