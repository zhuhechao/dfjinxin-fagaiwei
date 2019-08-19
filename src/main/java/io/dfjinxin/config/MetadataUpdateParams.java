package io.dfjinxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by GaoPh on 2019/6/25.
 */
@Data
@Component
@ConfigurationProperties(prefix = "metadata-update-params")
public class MetadataUpdateParams {
    private String runScript;
    private String jobType;
}
