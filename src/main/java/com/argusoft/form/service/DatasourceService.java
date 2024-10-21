package com.argusoft.form.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.DataSourceEntity;
import com.argusoft.form.repository.DatasourceRepository;

@Service
public class DatasourceService {

    @Autowired
    private DatasourceRepository datasourceRepository;

    public DatasourceService() {
    }

    public DataSourceEntity getDataSourceDetails(String role) {
        return datasourceRepository.findByRole(role);
    }

    public void addDataSource(DataSourceEntity dataSourceEntity) {
        datasourceRepository.save(dataSourceEntity);
    }

}
