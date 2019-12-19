package io.dfjinxin.modules.sys.service.impl;

//import io.dfjinxin.common.validator.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexValDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexValEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.analyse.service.WpPubOmService;
import io.dfjinxin.modules.price.controller.PssDatasetInfoController;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.SQLOutput;
import java.sql.Wrapper;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    private WpPubOmService wpPubOmService;

    @Autowired
    private WpBaseIndexValDao wpBaseIndexValDao;

    @Test
    public void pssPriceEwarnService() {
        pssPriceEwarnService.secondPageDetail(44);
    }

    @Test
    public void getprovinceLastDayMapData() {
//        String lastDayStr = DateUtils.dateToStr(DateUtils.addDateDays(new Date(), -1));

        Map<String, Object> yuQing = wpPubOmService.getYuQing(46, null, null);
        yuQing.forEach((k, v) -> System.out.println(k + " " + v));
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

    @Test
    public void test_mybatisPlus_select() {
        QueryWrapper<WpBaseIndexValEntity> where2 = new QueryWrapper();
        where2.select("index_id,comm_id,index_name,index_type");
        where2.eq("index_type", "价格");
        where2.eq("date", "2019-11-25");
        List<Map<String, Object>> wpBaseIndexValEntities = wpBaseIndexValDao.selectMaps(where2);
        wpBaseIndexValEntities.forEach(System.out::println);
    }

    @Test
    public void test_mybatisPlus_select2() {
        LambdaQueryWrapper<WpBaseIndexValEntity> where2 = Wrappers.lambdaQuery();
        where2.eq(WpBaseIndexValEntity::getIndexType, "价格");
        where2.eq(WpBaseIndexValEntity::getDate, "2019-11-25");
        List<Map<String, Object>> wpBaseIndexValEntities = wpBaseIndexValDao.selectMaps(where2);
        wpBaseIndexValEntities.forEach(System.out::println);
    }


}
