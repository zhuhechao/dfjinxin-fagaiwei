package io.dfjinxin.modules.sys.service.impl;

import io.dfjinxin.modules.hive.service.HiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/9/24 15:47
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHive {

    @Autowired
    private HiveService hiveService;

    @Test
    public void testquery() {

        String sql = "show tables";
        List<Map<String, Object>> tableLists = hiveService.selectData(sql);

        for (Map<String, Object> obj : tableLists) {
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
//                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
//                System.out.println(mapKey + ":" + mapValue);
                String sql2 = "select * from " + mapValue;
                List<Map<String, Object>> list = hiveService.selectData(sql2);
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("the result :"+list.get(i));
                }
            }
        }
    }
}
