/**
 * DataSourceConfig  2019/3/27
 * <p>
 * Copyright (c) 2018, DEEPEXI Inc. All rights reserved.
 * DEEPEXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.erp.project.config;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @description: 数据源配置
 * @program: DataSourceConfig
 * @author: donh
 * @mail: hudong@deepexi.com
 * @date: 2019/3/27 下午3:38
 **/
@Configuration
@MapperScan("com.erp.project.mapper")
public class DataSourceConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Primary
    @Bean(name = "dataSource", destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        final HikariDataSource hds = new HikariDataSource();
        hds.setJdbcUrl(jdbcUrl);
        hds.setDriverClassName(driverClassName);
        hds.setUsername(userName);
        hds.setPassword(password);
        return hds;
    }

}
