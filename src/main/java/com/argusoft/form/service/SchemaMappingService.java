package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.argusoft.form.entity.Schema;
import com.argusoft.form.repository.SchemaMappingRepo;

@Service
public class SchemaMappingService {

    private final SchemaMappingRepo schemaMappingRepo;

    public SchemaMappingService(SchemaMappingRepo schemaMappingRepo) {
        this.schemaMappingRepo = schemaMappingRepo;
    }

    public void addSchemaData(Schema schema) {
        schemaMappingRepo.save(schema);
    }

    public List<Schema> findAllSchema() {
        return schemaMappingRepo.findAll();
    }

    public List<Schema> findSchemaBySchemaName(String schema_name) {
        List<Schema> schemas = schemaMappingRepo.findBySchemaName(schema_name);
        return schemas;
    }

    public Optional<Schema> findSchemaByUUIDName(String uuid_name) {
        return schemaMappingRepo.findByUuidName(uuid_name);
    }

}
