package io.dfjinxin.modules.sys.service.impl;

//import io.dfjinxin.common.validator.Assert;

import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.controller.PssDatasetInfoController;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class SysMenuServiceImplTest {

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;
    @Autowired
    private PssDatasetInfoController controller;
    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    @Test
    public void pssPriceEwarnService() {
        pssPriceEwarnService.secondPageDetail(44);
    }

    @Test
    public void getprovinceLastDayMapData() {
        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));

        wpBaseIndexValService.getprovinceLastDayMapData(30, "价格", "2019-10-11");
    }

    @Test
    public void getMenuFromGovAuth() {
        pssPriceEwarnService.queryDetail(113, 18);
    }

    @Test
    public void saveDataSet() {
        PssDatasetInfoEntity entity = new PssDatasetInfoEntity();
        entity.setDataSetName("test1028 fhv");
        entity.setDataSetType(1);
        entity.setDataTime(new Date());
        String josnStr = "{\"indexIds\":[1,2,4,5]}";
        entity.setIndeVar(josnStr);
        System.out.println("indevar is : " + entity.getIndeVar());
        controller.saveDataSet(entity);
    }

}
