package com.argusoft.form.service;

import java.util.List;

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

    public DataSourceEntity getDataSourceDetails(String role, String schema) {

        List<DataSourceEntity> list = datasourceRepository.findByRoleAndSchemaName(role, schema);
        for (DataSourceEntity dataSourceEntity : list) {
            return dataSourceEntity;
        }
        return null;
    }

    public void addUserDataSource(DataSourceEntity dataSourceEntity) {
        datasourceRepository.save(dataSourceEntity);
    }

}
