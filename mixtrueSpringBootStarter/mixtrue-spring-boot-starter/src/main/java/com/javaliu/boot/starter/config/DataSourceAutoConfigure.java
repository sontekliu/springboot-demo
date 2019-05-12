package com.javaliu.boot.starter.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.javaliu.boot.starter.datasource.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(value = DataSourceProperties.class)
public class DataSourceAutoConfigure {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean //声明其为Bean实例
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        return datasource;
    }
}
