//package io.dfjinxin.modules.sys.service.impl;
//
////import io.dfjinxin.common.validator.Assert;
//import io.dfjinxin.modules.sys.entity.GovRootMenuEntity;
//import io.dfjinxin.modules.sys.service.SysMenuService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.util.Assert;
//
//import java.util.List;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
////由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
//public class SysMenuServiceImplTest {
//
//    @Autowired
//    private SysMenuService sysMenuService;
//
//    @Test
//    public void getMenuFromGovAuth() {
//        List<GovRootMenuEntity> menuFromGovAuth = sysMenuService.getMenuFromGovAuth((long) 111);
////        Assert.
//    }
//}