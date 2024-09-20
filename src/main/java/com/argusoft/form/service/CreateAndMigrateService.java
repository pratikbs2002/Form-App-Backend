package com.argusoft.form.service;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAndMigrateService {

    private final DataSource dataSource;
    private SchemaMappingService schemaMappingService;

    @Autowired
    public CreateAndMigrateService(DataSource dataSource, SchemaMappingService schemaMappingService) {
        this.dataSource = dataSource;
        this.schemaMappingService = schemaMappingService;
    }

    public String applyMigration(String schemaId) throws Exception {

        // try {
        // Connection connection = dataSource.getConnection();
        // String schemaId = "SCHEMA_" + UUID.randomUUID().toString().replace('-', '_');
        // String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS " + schemaId;
        // Statement stmt = connection.createStatement();
        // stmt.execute(createSchemaSQL);
        // connection.close();
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaId)
                .locations("classpath:com/argusoft/form/migration")
                .load();

        flyway.migrate();

        // } catch (SQLException e) {
        //     throw e;
        // }
        return "Migrations applied successfully";
    }

}
