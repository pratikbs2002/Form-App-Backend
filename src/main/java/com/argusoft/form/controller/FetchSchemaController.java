package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.security.datasource_config.UserContextHolder;
import com.argusoft.form.service.FetchSchemaService;
import com.argusoft.form.service.SchemaMappingService;

@RestController
@RequestMapping("/api/fetch-schema/")
public class FetchSchemaController {

    private final FetchSchemaService fetchSchemaService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SchemaMappingService schemaMappingService;

    public FetchSchemaController(FetchSchemaService fetchSchemaService) {
        this.fetchSchemaService = fetchSchemaService;

    }

    @GetMapping("/schema")
    public List<String> getSchemas() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return fetchSchemaService.getSchemas();
    }

    @GetMapping("/current-schema")
    public ResponseEntity<?> getCurrentSchema() throws SQLException {
        Map<String, String> responseMap = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            String schemaUUID = connection.getSchema();
            String schemaName = UserContextHolder.getSchema();
            responseMap.put("schemaUUID", schemaUUID);
            responseMap.put("schemaName", schemaName);
            return ResponseEntity.ok(responseMap);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving current schema");
        }
    }
}
