package io.dfjinxin.modules.firstpage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.job.task.WarnTask;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/10/09 15:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("price/second")
@Api(tags = "二级页面(商品总览)")
public class SecondPageViewController {

    private static Logger logger = LoggerFactory.getLogger(SecondPageViewController.class);

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;
    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    @Autowired
    WarnTask warnTask;


    /**
     * @Desc: 二级页面(商品总览)
     * @Param: 3类商品id[commId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/12 18:52
     */
    @GetMapping("/view/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-展示", notes = "根据3级商品id 获取相应该商品所有4级商品 指标信息 eg:58")
    public R queryIndexTypeByCommId(@PathVariable("") Integer commId) {
        logger.info("二级页面(商品总览),req commId:{commId}", commId);
//        指标类型信息
        List<Map<String, Object>> list = wpBaseIndexValService.secondPageIndexType(commId);
        Map<String, Object> resMap = new HashMap<>();
        for (Map<String, Object> var : list) {
            for (Map.Entry<String, Object> entry : var.entrySet()) {
                resMap.put(entry.getKey(), entry.getValue());
            }
        }

        //其它数据
        Map<String, Object> zfMap = pssPriceEwarnService.secondPageDetail(commId);
        resMap.putAll(zfMap);

        //统计3类品下 有哪些指标类类型是价格的规格品
        List<PssCommTotalEntity> type4commList = wpBaseIndexValService.queryCommListByCommId(commId, "价格");
        return R.ok().put("data", resMap).put("type4commList", type4commList);
    }

    /**
     * @Desc: 二级页面(商品总览)
     * @Param: 3类商品id[commId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/12 18:52
     */
    @GetMapping("/viewBy/{type}")
    @ApiOperation(value = "二级页面(商品总览)-展示", notes = "根据3级商品id 获取相应该商品所有4级商品 指标信息 eg:58")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexId", value = "规格品id", required = true, dataType = "List<String>", paramType = "query"),
            @ApiImplicitParam(name = "dateType", value = "日期格式", required = true, dataType = "String", paramType = "query")})

    public R viewBy(@PathVariable("type") String type,
                    @RequestParam("indexId") List<String> indexId,
                    @RequestParam("dateType") String dateType) {
        Map<String, Object> params = new HashMap();
        params.put("type", type);
        params.put("indexId", indexId);
        params.put("dateType", dateType);
        List<Map<String, Object>> data = pssPriceEwarnService.viewBy(params);
        return R.ok().put("data", data);
    }

    /**
     * @Desc: 二级页面(商品总览) -根据3类商品、时间区域、指标类型分页查询规格品指标信息
     * @Param: [commId, indexType, startDate, endDate, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/indexType/byDate/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-根据3类商品、时间区域、指标类型分页查询规格品指标信息",
            notes = "根据3级商品id 获取指定时间、指标类型规格品指标信息 eg:58")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Int", paramType = "query")
    })
    public R queryPageIndexValByDate(
            @PathVariable("commId") Integer commId,
            @RequestParam(value = "indexType", required = true) String indexType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize

    ) {
        Map<String, Object> params = new HashMap();
        params.put("indexType", indexType);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("commId", commId);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        PageUtils page = wpBaseIndexValService.queryPageByDate(params);
        if (params.get("indexType").toString().equals("价格")) {
            List<Map<String, Object>> lsi = wpBaseIndexValService.getPage(params);
            page.setList(lsi);
            page.setCurrPage(1);
            page.setPageSize(10);
            page.setTotalPage(10);
            page.setTotalCount(10);
        }
        List<PssCommTotalEntity> type4commList = wpBaseIndexValService.queryCommListByCommId(commId, indexType);
        return R.ok().put("page", page).put("type4CommList", type4commList);
    }

    /**
     * @Desc: 二级页面(商品总览) -根据3类商品、时间区域、指标类型分页查询规格品指标信息
     * @Param: [commId, indexType, startDate, endDate, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/indexType/download/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-根据3类商品、时间区域、指标类型分页查询规格品指标信息",
            notes = "根据3级商品id 获取指定时间、指标类型规格品指标信息 eg:58")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })
    public R downloadValByDate(
            @PathVariable("commId") Integer commId,
            @RequestParam(value = "indexType", required = true) String indexType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        Map<String, Object> params = new HashMap();
        params.put("indexType", indexType);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("commId", commId);
        List<Map<String, Object>> page = wpBaseIndexValService.downloadByDate(params);
        return R.ok().put("page", page);
    }


    /**
     * @Desc: 二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息
     * @Param: [commId, indexType, startDate, endDate]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/lineChart/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query")
    })
    public R queryLineChartByCondition(@PathVariable("commId") Integer commId,
                                       @RequestParam(value = "indexType") String indexType) {

        Map<String, Object> params = new HashMap();
        params.put("indexType", indexType);
        params.put("commId", commId);
        Map<String, Object> map = wpBaseIndexValService.queryLineChartByCondition(params);

        return R.ok().put("data", map);
    }

    /**
     * @Desc: 二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息
     * @Param: [commId, indexType, startDate, endDate]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/lineChartBy")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexId", value = "指标类型", required = true, dataType = "List<String>", paramType = "query"),
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query")
    })
    public R lineChartBy(@RequestParam(value = "indexId") List<String> indexId,
                         @RequestParam(value = "indexType") String indexType) {

        Map<String, Object> params = new HashMap();
        params.put("indexType", indexType);
        params.put("indexId", indexId);
        List<Map<String, Object>> map = wpBaseIndexValService.lineChartBy(params);

        return R.ok().put("data", map);
    }

    @GetMapping("/linejgBy/{type}")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "日期类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "indexId", value = "日期类型", required = true, dataType = "List<String>", paramType = "query")
    })
    public R linejgBy(@PathVariable(value = "type") String type,
                      @RequestParam(value = "dateType") String dateType,
                      @RequestParam(value = "indexId") List<String> indexId) {

        Map<String, Object> params = new HashMap();
        params.put("type", type);
        params.put("dateType", dateType);
        params.put("indexId", indexId);
        params.put("indexType", "价格");
        List<Map<String, Object>> map = wpBaseIndexValService.linejgBy(params);

        return R.ok().put("data", map);
    }

    @GetMapping("/provinceMap/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-价格指标页签-规格品各省份地图数据:根据规格品id、指标类型、日期 获取各省份数据",
            notes = "日期(yyyy-MM-dd)为空默认获取昨天数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    public R getProvinceMapByCommId(@PathVariable("commId") Integer commId,
                                    @RequestParam(value = "indexType") String indexType,
                                    @RequestParam(value = "date", required = false) String date) {
        List<WpBaseIndexValEntity> provinceMap = wpBaseIndexValService.getProvinceMapByCommId(commId, indexType, date);

        return R.ok().put("data", provinceMap);
    }


    @GetMapping("/test")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "指标类型", required = true, dataType = "String", paramType = "query")
    })
    public R test(@RequestParam(value = "id") String id) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(DateUtils.addDateDays(DateTime.getBeginOf(new Date()), -1)); //设置时间为当前时间
        ca.add(Calendar.YEAR, -1); //年份减1
        Date lastMonth = ca.getTime(); //结果
        try {
            warnTask.run(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok().put("data", lastMonth);
    }

    @GetMapping("/commIndexList/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    public R warningDistribution(@PathVariable("commId") Integer commId,
                                 @RequestParam(value = "year") String year) {
        Map<String, Object> ma = new HashMap<>();
        ma.put("commId", commId);
        ma.put("year", year);
        Map<String, Object> list1 = pssPriceEwarnService.getEwarValue(ma);
        List<Map<String, Object>> list = pssPriceEwarnService.warningDistribution(ma);
        return R.ok().put("data", list).put("ewarnSection", list1);
    }

    @GetMapping("/commIndexDate")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexId", value = "日期", required = false, dataType = "List<String>", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "日期", required = false, dataType = "String", paramType = "query")
    })
    public R commIndexDate(@RequestParam(value = "indexId") List<String> indexId,
                           @RequestParam(value = "year") String year) {
        Map<String, Object> ma = new HashMap<>();
        ma.put("indexId", indexId);
        ma.put("year", year);
        List<Map<String, Object>> list = pssPriceEwarnService.warningIndexDate(ma);
        return R.ok().put("data", list);
    }

    @GetMapping("/fore/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称(id)、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({})
    public R fore(@PathVariable("commId") String commId) {
        Map<String, Object> ma = new HashMap<>();
        ma.put("commId", commId);
        Map<String, Object> list = pssPriceEwarnService.fore(ma);
        return R.ok().put("data", list);
    }


}
