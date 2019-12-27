package io.dfjinxin.modules.model.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.model.service.ModelInfoService;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/12/25 11:07
 * @Version: 1.0
 */

@RestController
@RequestMapping("moxing")
@Api(tags = "模型管理-查询")
public class ModelQueryController {

    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;
    @Autowired
    private ModelInfoService modelInfoService;


    /**
     * @Desc:
     * @Param: [dataSetName, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/25 11:13
     */
    @GetMapping("/queryDataSet")
    @ApiOperation(value = "模型预处理-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName", value = "数据集名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Int", paramType = "query")
    })
    public R queryDataSet(
            @RequestParam(value = "dataSetName", required = false) String dataSetName,
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize

    ) {
        Map<String, Object> params = new HashMap() {{
            put("dataSetName", dataSetName);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        PageUtils page = pssDatasetInfoService.queryByPage(params);
        return R.ok().put("page", page);
    }

    /**
    * @Desc:  获取模型管理-算法列表
    * @Param: []
    * @Return: io.dfjinxin.common.utils.R
    * @Author: z.h.c
    * @Date: 2019/12/25 14:06
    */
    @ApiOperation(value = "获取模型管理-算法列表")
    @GetMapping("/getAlgorithmNames")
    public R getAlgorithm() {
        List<String> list = modelInfoService.getAlgorithm();

        return R.ok().put("data", list);
    }

    /**
     * @Desc: 模型管理-查询
     * @Param: [modelName, algorithm, predictTimeType, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/25 13:58
     */
    @GetMapping("/queryModel")
    @ApiOperation(value = "模型管理-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "模型名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "algorithm", value = "算法名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "predictTimeType", value = "预测周期", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Int", paramType = "query")
    })
    public R queryModel(
            @RequestParam(value = "modelName", required = false) String modelName,
            @RequestParam(value = "algorithm", required = false) String algorithm,
            @RequestParam(value = "predictTimeType", required = false) Integer predictTimeType,
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize

    ) {
        Map<String, Object> params = new HashMap() {{
            put("modelName", modelName);
            put("algorithm", algorithm);
            put("predictTimeType", predictTimeType);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        PageUtils page = modelInfoService.queryByPage(params);
        return R.ok().put("page", page);
    }

}
