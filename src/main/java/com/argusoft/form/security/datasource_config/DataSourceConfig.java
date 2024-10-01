package com.argusoft.form.security.datasource_config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource createRootDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/FormApp");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");

        // DriverManagerDataSource dataSource1 = new DriverManagerDataSource();
        // dataSource1.setDriverClassName("org.postgresql.Driver");
        // dataSource1.setUrl("jdbc:postgresql://localhost:5432/FormApp?currentSchema=india");
        // dataSource1.setUsername("user1");
        // dataSource1.setPassword("pass");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("postgres", dataSource);
        // targetDataSources.put("india", dataSource1);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        dynamicDataSource.setDefaultTargetDataSource(dataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);

        return dynamicDataSource;
    }
}