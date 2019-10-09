package io.dfjinxin.config.propertie;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.path.module")
public class ModulePathProperties {
    private String report;
    private String reportresult;

}
