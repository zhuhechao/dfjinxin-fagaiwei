package io.dfjinxin.modules.yuqing;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpPubOmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-18 10:56:59
 */
@RestController
@RequestMapping("yuqing/")
@Api(tags = "舆情分布(正面、负面、中立)")
public class WpPubOmController {
    @Autowired
    private WpPubOmService wpPubOmService;

    /**
     * @Desc: 根据3级商品id，统计该商品的情感分布(正面、负面、中立)数据
     * @Param: [commId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/18 11:02
     */
    @GetMapping("info/{commId}")
    @ApiOperation(value = "根据3级商品id，统计该商品的情感分布(正面、负面、中立)数据", notes = "时间为空默认查询一个月,时间格式:2019-12-18")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateFrom", value = "开始日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束日期", required = false, dataType = "String", paramType = "query")
    })
    public R getYuQingInfo(@PathVariable("commId") Integer commId,
                  @RequestParam(value = "dateFrom", required = false) String dateFrom,
                  @RequestParam(value = "dateTo", required = false) String dateTo) {
        Map<String, Object> data = wpPubOmService.getYuQing(commId, dateFrom, dateTo);

        return R.ok().put("data", data);
    }

}
