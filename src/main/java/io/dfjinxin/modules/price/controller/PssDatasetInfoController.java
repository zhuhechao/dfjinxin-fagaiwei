package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.hive.service.HiveService;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@RestController
@RequestMapping("price/pssdatasetinfo")
@Api(tags = "数据集信息")
public class PssDatasetInfoController {
    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;

    @Autowired
    private HiveService hiveService;

    @GetMapping("/getHiveTables")
    @ApiOperation("获取hive表数据")
    public R getHiveTables() {
        String sql = "show tables";
        List<Map<String, Object>> tableList = hiveService.selectData(sql);
        Map<String, Object> map = new HashMap<>();
        for (Map<String, Object> obj : tableList) {
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                Object tableName = entry.getValue();
                String sql2 = "select * from " + tableName;
                List<Map<String, Object>> list = hiveService.selectData(sql2);
                map.put(tableName.toString(), list);
            }
        }
        return R.ok().put("data", map);
    }

    /**
     * 列表
     */
    @GetMapping("/listAll")
    @RequiresPermissions("price:pssdatasetinfo:list")
    @ApiOperation("返回所有数据集")
    public R listAll(){
        List<PssDatasetInfoDto> list = pssDatasetInfoService.listAll();

        return R.ok().put("list", list);
    }

    /**
     * 列表
     */
    @PostMapping("/save")
    @RequiresPermissions("price:pssdatasetinfo:list")
    @ApiOperation("保存")
    public R save(@RequestBody PssDatasetInfoDto dto) {
        pssDatasetInfoService.saveOrUpdate(dto);
        return R.ok();
    }
}
