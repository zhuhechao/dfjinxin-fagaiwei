package io.dfjinxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gov-redirect")
public class GovRedirectConfig {
    private String cleanseWebUrl = "";
    private String desensitizeWebUrl = "";
    private String tagManagerWebUrl = "";
}
