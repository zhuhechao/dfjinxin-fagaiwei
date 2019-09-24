package io.dfjinxin.modules.hive.controller;

import io.dfjinxin.modules.hive.service.HiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hive")
public class HiveController {

    @Autowired
    private HiveService hiveService;

    @GetMapping("/test")
    public String test(String sql){
        hiveService.selectData(sql);
        return null;
    }
}
