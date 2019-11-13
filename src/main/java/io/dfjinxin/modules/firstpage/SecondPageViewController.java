package io.dfjinxin.modules.firstpage;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/10/09 15:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("price/second")
@Api(tags = "二级页面")
public class SecondPageViewController {

    private static Logger logger = LoggerFactory.getLogger(SecondPageViewController.class);

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;
    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    /**
    * @Desc:  二级页面
    * @Param: 3类商品id[commId]
    * @Return: io.dfjinxin.common.utils.R
    * @Author: z.h.c
    * @Date: 2019/11/12 18:52
    */
    @GetMapping("/view/{commId}")
    @ApiOperation(value = "二级页面-展示", notes = "根据3级商品id 获取相应该商品所有4级商品 指标信息 eg:58")
    public R queryIndexTypeByCommId(@PathVariable("commId") Integer commId) {
        logger.info("二级页面,req commId:{}", commId);
//        指标类型信息
        List<Map<String, Object>> list = wpBaseIndexValService.secondPageIndexType(commId);
        Map<String, Object> resMap = new HashMap<>();
        for (Map<String, Object> var : list) {
            for (String key : var.keySet()) {
                resMap.put(key, var.get(key));
            }
        }
        //其它数据
        Map<String, Object> zfMap = pssPriceEwarnService.secondPageDetail(commId);
        resMap.putAll(zfMap);
        return R.ok().put("data", resMap);
    }

}
