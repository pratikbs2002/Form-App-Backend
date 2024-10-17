package com.argusoft.form.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataSourceInspector {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void logCurrentDataSource() {
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            String currentSchema = jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
            System.out.println("Current DataSource: " + dataSource);
            System.out.println("Current Schema: " + currentSchema);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
