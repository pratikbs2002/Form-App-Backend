package com.argusoft.form.security.datasource_config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> targetDataSources = new HashMap<>();

    // Override method of abstract routing datasource which check the current look
    // up and it is responsible to set datasource based on look up key (Current
    // Tenant)
    @Override
    protected String determineCurrentLookupKey() {
        return UserContextHolder.getString();
    }

    // Method : To add datasource into TargetDataSources
    public void addDataSource(String username, DataSource dataSource) {
        targetDataSources.put(username, dataSource);
        Map<Object, Object> updatedTargetDataSources = new HashMap<>(super.getResolvedDataSources());
        updatedTargetDataSources.putAll(targetDataSources);

        super.setTargetDataSources(updatedTargetDataSources);
        super.afterPropertiesSet();
    }
}
