package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.Schema;
import com.argusoft.form.service.CreateAndMigrateService;
import com.argusoft.form.service.SchemaMappingService;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final CreateAndMigrateService createAndMigrateService;
    private SchemaMappingService schemaMappingService;
    private DataSource dataSource;

    @Autowired
    public SchemaController(CreateAndMigrateService createAndMigrateService, SchemaMappingService schemaMappingService,
            DataSource dataSource) {
        this.createAndMigrateService = createAndMigrateService;
        this.schemaMappingService = schemaMappingService;
        this.dataSource = dataSource;
    }

    @GetMapping("/mg/{schemaName}")
    public ResponseEntity<String> createAndMigrate(@PathVariable String schemaName) {
        try {
            // To generate schema uuid ........
            String schemaId = "schema_" + UUID.randomUUID().toString().replace('-', '_');

            // Check if schema already exists !.........
            List<Schema> schemaBySchemaName = schemaMappingService.findSchemaBySchemaName(schemaName);
            if (!schemaBySchemaName.isEmpty()) {
                return new ResponseEntity<>("Schema Already Present ... ", HttpStatus.OK);
            }

            // To creation of schema ...
            try {
                Connection connection = dataSource.getConnection();
                String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS " + schemaId;
                java.sql.Statement stmt = connection.createStatement();
                stmt.execute(createSchemaSQL);
                connection.close();
            } catch (SQLException e) {
                return new ResponseEntity<>(e.getMessage() + " : Error in Schema Creation ",
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }

            // To add schema data into schema mapping table ...
            Schema schema = new Schema(schemaId, schemaName);
            schemaMappingService.addSchemaData(schema);

            // After Schema Creation Apply Migration...
            String message = createAndMigrateService.applyMigration(schemaId);
            return new ResponseEntity<>(schemaName + " : Schema Created : " + message, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
