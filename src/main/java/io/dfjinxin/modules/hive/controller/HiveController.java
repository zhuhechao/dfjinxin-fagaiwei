//package io.dfjinxin.modules.hive.controller;
//
//import io.dfjinxin.common.utils.R;
//import io.dfjinxin.modules.hive.service.HiveService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/hive")
//@Api(tags = "HiveController", description = "数据集")
//public class HiveController {
//
//    /*@Autowired
//    private HiveService hiveService;
//
//    @GetMapping("/getHiveTables")
//    @ApiOperation("获取hive表数据")
//    public R getHiveTables() {
//        String sql = "show tables";
//        List<Map<String, Object>> tableList = hiveService.selectData(sql);
//        Map<String, Object> map = new HashMap<>();
//        for (Map<String, Object> obj : tableList) {
//            for (Map.Entry<String, Object> entry : obj.entrySet()) {
//                Object tableName = entry.getValue();
//                String sql2 = "select * from " + tableName;
//                List<Map<String, Object>> list = hiveService.selectData(sql2);
//                map.put(tableName.toString(), list);
//            }
//        }
//        return R.ok().put("data", map);
//    }*/
//
//}
