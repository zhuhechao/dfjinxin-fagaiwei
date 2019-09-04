package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyrelt")
@Api(tags = "PssAnalyReltController", description = "分析结果")
public class PssAnalyReltController {
    @Autowired
    private PssAnalyReltService pssAnalyReltService;

    /**
     * 查询分析结果
     */
    @GetMapping("/query")
    @RequiresPermissions("price:pssanalyrelt:query")
    @ApiOperation("查询分析结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "analyName", value = "分析名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "analyWay", value = "分析类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "datasetId", value = "数据集id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据量", required = false, dataType = "Int", paramType = "query")
    })
    public R query(@RequestParam(value = "analyName", required = false) String analyName,
                   @RequestParam(value = "analyWay", required = false) Integer analyWay,
                   @RequestParam(value = "datasetId", required = false) Integer datasetId,
                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Map<String, Object> params = new HashMap() {{
            put("analyName", analyName);
            put("analyWay", analyWay);
            put("datasetId", datasetId);
            put("start", (pageIndex - 1) * pageSize);
            put("pageSize", pageSize);
        }};
        PageUtils page = pssAnalyReltService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/{reltId}")
    @RequiresPermissions("price:pssanalyrelt:info")
    @ApiOperation("获取详情")
    public R info(@PathVariable("reltId") String reltId){
        PssAnalyReltEntity pssAnalyRelt = pssAnalyReltService.getById(reltId);

        return R.ok().put("pssAnalyRelt", pssAnalyRelt);
    }
}
