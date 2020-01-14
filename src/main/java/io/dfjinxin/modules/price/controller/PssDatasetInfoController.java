package io.dfjinxin.modules.price.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.python.PythonApiUtils;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.dfjinxin.modules.sys.controller.AbstractController;
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
public class PssDatasetInfoController extends AbstractController {

    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;

    @Autowired
    private PssDatasetInfoDao pssDatasetInfoDao;

    @Autowired
    private HiveService hiveService;

    @Value("${python.url}")
    private String pyUrl;

    private static final Logger LOG = LoggerFactory.getLogger(PssDatasetInfoController.class);

    /**
     * 列表
     * modify by zhc 2020.1.14
     */
    @GetMapping("/listAll")
    @ApiOperation("返回所有数据集")
    public R listAll() {
        List<PssDatasetInfoEntity> list = pssDatasetInfoService.listAll();
        /*for (PssDatasetInfoEntity pssDatasetInfoEntity : list) {
            pssDatasetInfoService.setPssDatasetInfoIndeName(pssDatasetInfoEntity);
        }*/
        list.forEach(entity -> pssDatasetInfoService.converValAndName(entity));
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

        LOG.info("数据集创建-start");
        if (entity == null || StringUtils.isEmpty(entity.getDataSetName())) {
            return R.error("创建数据集参数为空!");
        }

        QueryWrapper<PssDatasetInfoEntity> where = new QueryWrapper();
        where.eq("data_set_name", entity.getDataSetName());
        where.isNotNull("data_set_eng_name");
        int recordCount = pssDatasetInfoDao.selectCount(where);
        if (recordCount > 0) return R.error("数据集名称重复!");

        final String api = "createDataSet";
        LOG.info("调用python-[{}]服务,请求参数-{}", api, entity.getIndeVar());
        long startTime = System.currentTimeMillis();
        String result = null;
        try {
            result = PythonApiUtils.doPost(pyUrl + api, entity.getIndeVar());
        } catch (Exception e) {
            return R.error("调用python-" + api + "服务异常。创建失败!");
        }
        Long time = (System.currentTimeMillis() - startTime) / 1000;
        LOG.info("调用python-[{}]结束,用时:{}秒!", api, time);

        if (StringUtils.isEmpty(result)) {
            return R.error("数据集创建失败!");
        }

        JSONObject jsonObj = JSON.parseObject(result);
        String code = jsonObj.containsKey("code") ? jsonObj.getString("code") : null;
        if ("succ".equals(code)) {
            String tableName = jsonObj.containsKey("name") ? jsonObj.getString("name") : null;
            String shape = jsonObj.containsKey("shape") ? jsonObj.getString("shape") : null;
            String indevar = jsonObj.containsKey("exist_ids") ? jsonObj.getString("exist_ids") : null;
            entity.setDataSetEngName(tableName);
            entity.setShape(shape);
            entity.setIndeVar(indevar);

            //TODO
            //entity.setUserId(super.getUserId());
            entity.setUserId("2");
            pssDatasetInfoService.save(entity);
            LOG.info("数据集创建-成功!");
            return R.ok();
        } else {
            LOG.info("数据集创建-失败!");
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
    @PostMapping("/update")
    @ApiOperation("修改-修改inde_val字段值")
    public R update(@RequestBody PssDatasetInfoEntity entity) {

        if (entity == null) return null;
        PssDatasetInfoEntity queryEntity = pssDatasetInfoService.getById(entity.getDataSetId());
        if (queryEntity == null) {
            return R.error("数据集,不存在!");
        }
        entity.setIndeVar(entity.getIndeVar());
        entity.setCommIndevalPath(entity.getCommIndevalPath());
        entity.setMacroIndevalPath(entity.getMacroIndevalPath());
        entity.setDataTime(new Date());
        //TODO
//        entity.setUserId(super.getUserId());
        entity.setUserId("1");
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
    public R delete(@PathVariable("dataSetId") Integer dataSetId) {
        PssDatasetInfoEntity entity = pssDatasetInfoService.getById(dataSetId);
        if (entity == null) {
            return R.error("数据集-" + dataSetId + ",不存在!");
        }

        //数据集所在库名
        final String databaseName = "zhjg_dataset.";
        hiveService.dropTable(databaseName + entity.getDataSetEngName());
        pssDatasetInfoService.removeById(dataSetId);
        return R.ok();
    }

    /**
     * @Desc: 数据集-查询已创建的数据集列表
     * @Param: []
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/28 17:54
     */
    @GetMapping("/getDataSetList")
    @ApiOperation("数据集页面-获取已创建的数据集列表")
    public R getDataSetList() {
        return R.ok().put("data", pssDatasetInfoService.getDataSetList());
    }

    /**
     * @Desc: 数据集-根据指定数据集id查询指标(商品指标信息&宏观指标信息)信息
     * @Param: []
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/28 17:54
     */
    @GetMapping("/getIndexInfoByDataSetId/{dataSetId}")
    @ApiOperation("数据集页面-根据数据集dataSetId查询指标(商品指标信息&宏观指标信息)信息")
    public R getIndexInfoByDataSetIndeVal(@PathVariable("dataSetId") Integer dataSetId) {

        PssDatasetInfoEntity entity = pssDatasetInfoService.getById(dataSetId);
        if (entity == null) {
            return R.error("数据集-" + dataSetId + ",不存在!");
        }

        return R.ok().put("data", pssDatasetInfoService.getIndexInfoByDataSetIndeVal(entity.getIndeVar()));
    }
}
