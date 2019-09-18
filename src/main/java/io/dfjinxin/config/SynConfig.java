//package io.dfjinxin.config;
//
//import io.dfjinxin.modules.sys.syndata.SynInterceptor.SynDataInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by GaoPh on 2019/9/5.
// */
//@Configuration
//public class SynConfig extends WebMvcConfigurationSupport {
//    @Autowired
//    private SynDataInterceptor synDataInterceptor;
//
//    protected void addInterceptors(InterceptorRegistry registry){
//        List<String> list = new ArrayList<>();
//        list.add("/zhjg/synch/**");
//        list.add("/zhjg/synch");
//        registry.addInterceptor(synDataInterceptor).addPathPatterns(list);
//        super.addInterceptors(registry);
//    }
//}
