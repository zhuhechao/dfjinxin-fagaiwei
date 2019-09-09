/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin;

import io.dfjinxin.config.propertie.AppPathProperties;
import io.dfjinxin.config.propertie.AppProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "io.dfjinxin")
@MapperScan(value = "io.dfjinxin.**.dao")
@EnableTransactionManagement
@EnableCaching
@EnableConfigurationProperties({AppProperties.class, AppPathProperties.class})
public class FaGaiWeiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaGaiWeiApplication.class, args);
	}

}