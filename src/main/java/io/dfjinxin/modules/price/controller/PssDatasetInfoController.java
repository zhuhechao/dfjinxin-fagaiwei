package io.dfjinxin.modules.price.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.python.PythonApiUtils;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Desc: 从hive获取数据表，用户选择表中字段的值组合。
 * 作为数据集落地到应用库
 * 流程：
 * step1,用户前端提交数据集组合，后台调python逻辑
 * step2,根据python结果，成功数据集入应用库，失败不入库
 * @Param:
 * @Return:
 * @Author: z.h.c
 * @Date: 2019/10/11 10:47
 */
@RestController
@RequestMapping("price/pssdatasetinfo")
@Api(tags = "数据集信息")
public class PssDatasetInfoController {

    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;
    @Autowired
    private HiveService hiveService;
    @Autowired
    private WpBaseIndexInfoService wpBaseIndexInfoService;

    @Value("${ssh.user}")
    private String userName;
    @Value("${ssh.host}")
    private String host;
    @Value("${ssh.pass}")
    private String pass;
    @Value("${ssh.port}")
    private int port;

    @Value("${ssh.url}")
    private String pyUrl;

    private static Logger Log = LoggerFactory.getLogger(PssDatasetInfoController.class);

    /**
     * 列表
     */
    @GetMapping("/listAll")
    @ApiOperation("返回所有数据集")
    public R listAll() {
        List<PssDatasetInfoEntity> list = pssDatasetInfoService.listAll();
        for (PssDatasetInfoEntity pssDatasetInfoEntity : list) {
            pssDatasetInfoService.setPssDatasetInfoIndeName(pssDatasetInfoEntity);
        }
        return R.ok().put("list", list);
    }

    /**
     * 列表
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    public R saveDataSet(@RequestBody PssDatasetInfoEntity entity) {
        Log.info("数据集创建-start");
        String api = "createDataSet";
        String result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = PythonApiUtils.doPost(pyUrl + api, entity.getIndeVar());
        } catch (Exception e) {
            return R.error("调用python-" + api + "服务异常。创建失败!");
        }
        Log.info("调用python-{}结束,用时:{}", api, (System.currentTimeMillis() - startTime) / 1000 + "秒!");

        if (StringUtils.isEmpty(result)) {
            return R.error("数据集创建失败!");
        }

        JSONObject jsonObj = JSON.parseObject(result);
        String code = jsonObj.getString("code");
        String tableName = jsonObj.getString("data");
        if ("succ".equals(code) && StringUtils.isNotEmpty(tableName)) {
            entity.setDataSetEngName(tableName);
            pssDatasetInfoService.save(entity);
            return R.ok();
        } else {
            return R.error("数据集创建失败!");
        }
    }

    @PostMapping("/delete/{dataSetId}")
    @ApiOperation("删除")
    public R delete(@PathVariable Integer dataSetId) {
        pssDatasetInfoService.removeById(dataSetId);
        return R.ok();
    }
}
