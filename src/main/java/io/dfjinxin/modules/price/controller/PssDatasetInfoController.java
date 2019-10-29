package io.dfjinxin.modules.price.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.dfjinxin.modules.price.service.SSHConnect;
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

    @Value("${ssh.user}")
    private String userName;
    @Value("${ssh.host}")
    private String host;

    @Value("${ssh.pass}")
    private String pass;

    @Value("${ssh.port}")
    private int port;

    private static Logger Log = LoggerFactory.getLogger(PssDatasetInfoController.class);


    /**
     * @Desc: 获取大数据平台hive数据表
     * @Param:
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/10/11 11:14
     */
    /*@GetMapping("/getHiveData/{tableName}")
    @ApiOperation("根据hive表名获取数据")
    public R getHiveTables(@PathVariable("tableName") String tableName) {
        String sql = "show tables";
        String limit = " limit 10";
        Map<String, Object> map = new HashMap<>();
        *//*List<Map<String, Object>> tableList = hiveService.selectData(sql);
        for (Map<String, Object> obj : tableList) {
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                Object tableName = entry.getValue();
                String sql2 = "select * from " + tableName.toString() + limit;
                List<Map<String, Object>> list = hiveService.selectData(sql2);
                map.put(tableName.toString(), list);
            }
        }*//*

        String sql2 = "select * from " + tableName + limit;
        List<Map<String, Object>> list = hiveService.selectData(sql2);
        return R.ok().put("data", list);
    }

    @GetMapping("/getHiveTableNames")
    @ApiOperation("获取hive表名")
    public R getHiveTableNames() {
        String sql = "show tables";
        // pss_comm_total,wp_index_base_info
        List<Map<String, Object>> tableNameList = hiveService.selectData(sql);
        return R.ok().put("data", tableNameList);
    }*/

    /**
     * 列表
     */
    @GetMapping("/listAll")
    @ApiOperation("返回所有数据集")
    public R listAll() {
        List<PssDatasetInfoDto> list = pssDatasetInfoService.listAll();

        return R.ok().put("list", list);
    }

    /**
     * 列表
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    public R saveDataSet(@RequestBody PssDatasetInfoEntity entity) {
        Log.info("数据集创建-start");
        Long startTime = new Date().getTime();
        String pyFileName = "/home/ndrc-test/unstack_single_table.py";
        String result = null;
        try {
            result = SSHConnect.callPyProc(pyFileName, entity.getIndeVar(), host, userName, pass, port);
        } catch (Exception e) {
            return R.error("调用python-{},文件异常。创建失败!");
        }
        Log.info("调用文件:{}结束,用时:{}", pyFileName, (new Date().getTime() - startTime) + " 秒!");

        JSONObject jsonObj = JSON.parseObject(result);
        String code = jsonObj.getString("code");
        String tableName = jsonObj.getString("res");
        if ("succ".equals(code) && StringUtils.isNotEmpty(tableName)) {
            entity.setDataSetEngName(tableName);
            pssDatasetInfoService.save(entity);
        }
        return R.error("数据集创建失败!");
    }

    @PostMapping("/delete/{dataSetId}")
    @ApiOperation("删除")
    public R delete(@PathVariable Integer dataSetId) {
        pssDatasetInfoService.removeById(dataSetId);
        return R.ok();
    }
}
