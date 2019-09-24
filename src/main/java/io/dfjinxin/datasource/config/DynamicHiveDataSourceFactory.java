/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.dfjinxin.datasource.properties.HiveSourceProperties;

/**
 * DruidDataSource
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class DynamicHiveDataSourceFactory {

    public static DruidDataSource buildDruidDataSource(HiveSourceProperties hiveSourceProperties) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(hiveSourceProperties.getUrl());
        datasource.setUsername(hiveSourceProperties.getUser());
        datasource.setPassword(hiveSourceProperties.getPassword());
        datasource.setDriverClassName(hiveSourceProperties.getDriverClassName());

        // pool configuration
        datasource.setInitialSize(hiveSourceProperties.getInitialSize());
        datasource.setMinIdle(hiveSourceProperties.getMinIdle());
        datasource.setMaxWait(hiveSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(hiveSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(hiveSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(hiveSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(hiveSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(hiveSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(hiveSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(hiveSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(hiveSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        return datasource;
    }
}
