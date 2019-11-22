package io.dfjinxin.modules.price.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.python.PythonApiUtils;
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

import java.util.Date;
import java.util.List;

/**
 * @Desc:
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
     * @Desc: 流程：
     * * step1,用户前端提交商品指标id 宏观指标id，后台调python逻辑
     * * step2,根据python结果，成功数据集入应用库，失败不入库
     * @Param: [entity]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/22 17:24
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    public R saveDataSet(@RequestBody PssDatasetInfoEntity entity) {
        Log.info("数据集创建-start");
        String api = "createDataSet";
        long startTime = System.currentTimeMillis();
        String result = null;
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

    /**
     * @Desc: 数据集修改-只修改inde_val字段值
     * @Param: [entity]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/22 16:03
     */
    @PostMapping("/update/{dataSetId}")
    @ApiOperation("修改-修改inde_val字段值")
    public R update(@PathVariable Integer dataSetId, @RequestParam final String indeVal) {

        PssDatasetInfoEntity entity = pssDatasetInfoService.getById(dataSetId);
        if (entity == null) {
            return R.error("数据" + dataSetId + ",不存在!");
        }
        entity.setIndeVar(indeVal);
        entity.setDataTime(new Date());
        pssDatasetInfoService.updateById(entity);
        return R.ok();
    }

    /**
     * @Desc: 删除应用库记录、hive上对应data_set_eng_name 表
     * @Param: [dataSetId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/22 16:10
     */
    @PostMapping("/delete/{dataSetId}")
    @ApiOperation("删除")
    public R delete(@PathVariable Integer dataSetId) {
        PssDatasetInfoEntity entity = pssDatasetInfoService.getById(dataSetId);
        if (entity == null) {
            return R.error("数据" + dataSetId + ",不存在!");
        }

        hiveService.dropTable(entity.getDataSetEngName());
        pssDatasetInfoService.removeById(dataSetId);
        return R.ok();
    }
}
