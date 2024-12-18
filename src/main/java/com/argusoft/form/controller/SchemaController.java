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

import com.argusoft.form.entity.DataSourceEntity;
import com.argusoft.form.entity.Role;
import com.argusoft.form.entity.Schema;
import com.argusoft.form.entity.User;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.enums.RoleEnum;
import com.argusoft.form.service.CreateAndMigrateService;
import com.argusoft.form.service.DatasourceService;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.RoleService;
import com.argusoft.form.service.SchemaMappingService;
import com.argusoft.form.service.UserInfoService;
import com.argusoft.form.service.UserService;

import jakarta.transaction.Transactional;

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
    private UserInfoService userInfoService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DbUserRegistrationService dbUserRegistrationService;

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SchemaController(CreateAndMigrateService createAndMigrateService, SchemaMappingService schemaMappingService,
            DataSource dataSource) {
        this.createAndMigrateService = createAndMigrateService;
        this.schemaMappingService = schemaMappingService;
        this.dataSource = dataSource;
    }

    @GetMapping("/mg/{schemaName}")
    @Transactional
    public ResponseEntity<?> createAndMigrate(@PathVariable String schemaName) {
        try {
            // To generate schema uuid ........
            String schemaId = "_" + UUID.randomUUID().toString().replace('-', '_');

            // Check if schema already exists !.........
            List<Schema> schemaBySchemaName = schemaMappingService.findSchemaBySchemaName(schemaName);
            if (!schemaBySchemaName.isEmpty()) {
                return new ResponseEntity<>("Schema Already Present ... ", HttpStatus.BAD_REQUEST);
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

            // Start with the default user registration...
            User adminUser = new User();
            adminUser.setUsername(schemaId + "_" + schemaName + "_admin");
            adminUser.setSchemaName(schemaId);
            Role adminRole = roleService.findRoleByName(RoleEnum.ADMIN);
            adminUser.setRole(adminRole);
            adminUser.setPassword(passwordEncoder.encode("admin"));

            // Create Database Admin user
            try {
                dbUserRegistrationService.registerAdminDbUser(adminUser);
            } catch (SQLException e) {
                return ResponseEntity.badRequest().body(e.getMessage() + "Somthing Wrong!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Registration failed: " + e.getMessage());
            }

            User reportingUser = new User();
            reportingUser.setUsername(schemaId + "_" + schemaName + "_reporting_user");
            reportingUser.setSchemaName(schemaId);
            Role reportingRole = roleService.findRoleByName(RoleEnum.REPORTING_USER);
            reportingUser.setRole(reportingRole);
            reportingUser.setPassword(passwordEncoder.encode("rep"));

            // Create Database Reporting user
            try {
                dbUserRegistrationService.registerReportingDbUser(reportingUser);
            } catch (SQLException e) {
                return ResponseEntity.badRequest().body(e.getMessage() + "Somthing Wrong!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Registration failed: " + e.getMessage());
            }

            // To add admin role datasource into datasource table
            DataSourceEntity adminDatasource = new DataSourceEntity();
            adminDatasource.setSchemaName(schemaId);
            adminDatasource.setUsername(adminUser.getUsername());
            adminDatasource.setPassword(adminUser.getPassword());
            adminDatasource.setRole(adminUser.getRole().getRoleName().toString());

            datasourceService.addUserDataSource(adminDatasource);

            System.out.println(adminDatasource);

            // To add reporting role datasource into datasource table
            DataSourceEntity reportingUserDatasource = new DataSourceEntity();
            reportingUserDatasource.setSchemaName(schemaId);
            reportingUserDatasource.setUsername(reportingUser.getUsername());
            reportingUserDatasource.setPassword(reportingUser.getPassword());
            reportingUserDatasource.setRole(reportingUser.getRole().getRoleName().toString());

            System.out.println(reportingUserDatasource);
            datasourceService.addUserDataSource(reportingUserDatasource);

            // Add Database Admin user details in public schema user table
            adminUser.setUsername("default_" + schemaName + "_admin");
            userService.registerNewUser(adminUser);

            // Add Database Reporting user details in public schema user table
            reportingUser.setUsername("default_" + schemaName + "_reporting_user");
            userService.registerNewUser(reportingUser);

            // Add Database Admin user details in specific schema user table
            // UserInfo adminUserInfo = new UserInfo();
            // adminUserInfo.setFirstName("temp");
            // adminUserInfo.setRole(new Role((Long) 1L, RoleEnum.ADMIN));

            // userInfoService.createUser(adminUserInfo);

            // Add Database Reporting user details in specific schema user table
            // UserInfo reportingUserInfo = new UserInfo();
            // adminUserInfo.setFirstName("temp");
            // adminUserInfo.setRole(new Role((Long) 2L, RoleEnum.REPORTING_USER));

            // userInfoService.createUser(reportingUserInfo);

            Map<String, Object> mp = new HashMap<>();
            // mp.put("User", reportingUser);
            // mp.put("SchemaName", schemaName);
            // mp.put("SchemaID", schemaId);
            mp.put("message", "schema created successfully." + message);

            return new ResponseEntity<>(mp, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
