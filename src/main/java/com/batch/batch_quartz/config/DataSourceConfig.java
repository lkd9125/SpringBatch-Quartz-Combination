package com.batch.batch_quartz.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.metadata")
    @Primary
    public DataSource metaDataSource(){
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.services")
    public DataSource serviceDataSource(){
        HikariDataSource dataSource = DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
        
        return dataSource;
    }


}