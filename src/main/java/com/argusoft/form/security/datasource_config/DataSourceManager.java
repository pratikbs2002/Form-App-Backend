package com.argusoft.form.security.datasource_config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

@Service
public class DataSourceManager {
    private final Map<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DynamicDataSource dynamicDataSource;

    // Method: To Take Datasource properties and set into target datasource of  abstract datasource routing
    public DataSource getDataSource(String schemaName, String username, String password) {
        return dataSources.computeIfAbsent(username, key -> {
            DataSource newDataSource = createDataSource(schemaName, username, password);
            dynamicDataSource.addDataSource(username, newDataSource);
            return newDataSource;
        });
    }

    // Method : To Create Datasource based on given properties
    private DataSource createDataSource(String schemaName, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/formapp?currentSchema=" +
                schemaName;
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
