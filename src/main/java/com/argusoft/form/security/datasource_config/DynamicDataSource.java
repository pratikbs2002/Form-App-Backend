package com.argusoft.form.security.datasource_config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> targetDataSources = new HashMap<>();

    @Override
    protected String determineCurrentLookupKey() {
        return UserContextHolder.getString();
    }

    public void addDataSource(String username, DataSource dataSource) {
        targetDataSources.put(username, dataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }
}
