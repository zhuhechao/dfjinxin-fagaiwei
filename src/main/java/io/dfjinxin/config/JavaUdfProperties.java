package io.dfjinxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "java.udf")
public class JavaUdfProperties {

    private String source; //源文件存放路径
    private String path; //文件生成后存放路径
    private String pkg; //包名
    private String name; //源java udf文件类名
    private String script; //需要替换的script源码
    private String funcname; //需要替换的script函数名
//    private String javac;
//    private String jar;
//    private String dependencyJar;//hive-exec-3.1.1.jar     #依赖编译的包
//    private String hdfs;
//    private String jdbc;
//    private String user;
}
