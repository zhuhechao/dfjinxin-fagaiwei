/**
 * 2019 东方金信
 *
 *
 *
 *
 */


package io.dfjinxin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;


/**
 * Filter配置
 * @author Mark sunlightcs@gmail.com
 */

@Configuration
@ConditionalOnProperty(prefix = "ca", name = "valid", havingValue = "true", matchIfMissing = true)
public class FilterConfig {
    private Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        logger.info("shiroFilterRegistration is running ");

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(0);
        registration.addUrlPatterns("/*");
        return registration;
    }
}

