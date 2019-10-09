package io.dfjinxin.config.propertie;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.path")
public class AppPathProperties {

    private String temp;
    private String upload;
    private String workDir;
    private String userDir;
    private ModulePathProperties module;

}
