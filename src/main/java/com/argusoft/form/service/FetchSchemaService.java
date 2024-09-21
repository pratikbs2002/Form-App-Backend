package com.argusoft.form.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.repository.FetchSchemaRepo;

@Service
public class FetchSchemaService {

    private final FetchSchemaRepo fetchSchemaRepo;

    @Autowired
    public FetchSchemaService(FetchSchemaRepo fetchSchemaRepo) {
        this.fetchSchemaRepo = fetchSchemaRepo;
    }

    public List<String> getSchemas() {
        try {
            return fetchSchemaRepo.getSchemas();
        } catch (SQLException e) {
            System.out.println("Something Wrong While Fetching Schemas.......");
        }
        return null;
    }

}
