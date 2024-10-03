package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.Schema;
import com.argusoft.form.entity.User;
import com.argusoft.form.service.CreateAndMigrateService;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.SchemaMappingService;
import com.argusoft.form.service.UserService;

@RestController
@RequestMapping("/api/schema")
@CrossOrigin("*")
public class SchemaController {

    private final CreateAndMigrateService createAndMigrateService;
    private final SchemaMappingService schemaMappingService;
    private final DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private DbUserRegistrationService dbUserRegistrationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SchemaController(CreateAndMigrateService createAndMigrateService, SchemaMappingService schemaMappingService,
            DataSource dataSource) {
        this.createAndMigrateService = createAndMigrateService;
        this.schemaMappingService = schemaMappingService;
        this.dataSource = dataSource;
    }

    @GetMapping("/mg/{schemaName}")
    public ResponseEntity<?> createAndMigrate(@PathVariable String schemaName) {
        try {
            // To generate schema uuid ........
            String schemaId = "_" + UUID.randomUUID().toString().replace('-', '_');

            // Check if schema already exists !.........
            List<Schema> schemaBySchemaName = schemaMappingService.findSchemaBySchemaName(schemaName);
            if (!schemaBySchemaName.isEmpty()) {
                return new ResponseEntity<>("Schema Already Present ... ", HttpStatus.OK);
            }

            // To creation of schema ...

            try (Connection connection = dataSource.getConnection()) {
                String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS " + schemaId;
                Statement stmt = connection.createStatement();
                stmt.execute(createSchemaSQL);

            } catch (SQLException e) {
                return new ResponseEntity<>(e.getMessage() + " : Error in Schema Creation ",
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }

            // To add schema data into schema mapping table ...
            Schema schema = new Schema(schemaId, schemaName);
            schemaMappingService.addSchemaData(schema);

            // After Schema Creation Apply Migration...
            String message = createAndMigrateService.applyMigration(schemaId);

            User user = new User();
            user.setUsername(schemaId + "_" + schemaName + "_admin");
            user.setSchemaName(schemaId);
            user.setPassword(passwordEncoder.encode("admin"));

            // Create Databas user
            try {
                dbUserRegistrationService.registerDbUser(user);
            } catch (SQLException e) {
                return ResponseEntity.ok(e.getMessage() + "Somthing Wrong!");
            }

            // Add Database user details in public schema user table
            userService.registerNewUser(user);

            Map<String, Object> mp = new HashMap<>();
            mp.put("User", user);
            mp.put("SchemaName", schemaName);
            mp.put("SchemaID", schemaId);
            mp.put("Message", message);

            return new ResponseEntity<>(mp, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
