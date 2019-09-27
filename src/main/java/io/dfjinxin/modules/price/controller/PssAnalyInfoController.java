package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyinfo")
@Api(tags = "分析信息")
public class PssAnalyInfoController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    @Autowired
    private PssAnalyReltService pssAnalyReltService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssanalyinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = pssAnalyInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{analyId}")
    @RequiresPermissions("price:pssanalyinfo:info")
    public R info(@PathVariable("analyId") Integer analyId) {
        PssAnalyInfoEntity pssAnalyInfo = pssAnalyInfoService.getById(analyId);

        return R.ok().put("pssAnalyInfo", pssAnalyInfo);
    }

    /**
     * 运行
     */
    @PostMapping("/run")
    @ApiOperation("运行")
    public R run(@RequestBody PssAnalyInfoDto dto) {
        pssAnalyInfoService.saveOrUpdate(dto);

        PssAnalyReltEntity relt = new PssAnalyReltEntity();
        pssAnalyReltService.saveOrUpdate(null);
        return R.ok();
    }

    @PostMapping("/testPy")
    @ApiOperation("测试调用python")
    public R testCallPy() {
        List<String> list = new ArrayList();
        logger.debug("调用 python start");
        String file = "/zhjg/pyjiaoben/corr_ana.py";
        logger.debug("file path:" + file);
        String[] args = new String[]{"python", file, "/zhjg/", "猪生产价格指数&粮食产量&粮食价格指数&人均纯收入&人均猪肉消费量&存栏数&猪肉产量"};
        for (String str : args) {
            logger.debug("call py arg:" + str);
        }
        try {
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                logger.debug("call result the result:" + line);
                list.add(line);
            }
            in.close();
            proc.waitFor();
            logger.debug("调用 end");
        } catch (Exception e1) {
            logger.debug("调用 py异常");
            logger.debug(e1.getMessage());
        }
        return R.ok().put("data", list);
    }


    @GetMapping("/bussType/{bussType}")
    @ApiOperation(value = "根据业务类型查询分析类型", notes = "1:相关性分析;2:因果分析")
    public R getAnalyWayByBussType(@PathVariable("bussType") Integer bussType) {
        List<PssAnalyReltEntity> analyWayList = pssAnalyInfoService.getAnalyWayByBussType(bussType);
        return R.ok().put("data", analyWayList);
    }

    @GetMapping("/analyWay/{AnalyWay}")
    @ApiOperation(value = "根据分析类型查询该类型的结果集")
    public R getDataSetByAnalyWay(@PathVariable("AnalyWay") String AnalyWay) {
        List<PssDatasetInfoEntity> dataSetList = pssAnalyInfoService.getDataSetByAnalyWay(AnalyWay);
        return R.ok().put("data", dataSetList);
    }

}
