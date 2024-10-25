package com.argusoft.form.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.Schema;
import com.argusoft.form.entity.User;
import com.argusoft.form.service.SchemaMappingService;

@RestController
@RequestMapping("/api/schema-map")
public class SchemaMappingController {

    private final SchemaMappingService schemaMappingService;

    public SchemaMappingController(SchemaMappingService schemaMappingService) {
        this.schemaMappingService = schemaMappingService;
    }

    @GetMapping("/add")
    public void addSchemaData(Schema schema) {
        schemaMappingService.addSchemaData(schema);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllSchema() {
        return new ResponseEntity<>(schemaMappingService.findAllSchema(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/pageable/all")
    public ResponseEntity<?> findPageableAllSchema(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<List<Schema>> schemas = schemaMappingService.findPageableAllSchema(pageable);

            if (schemas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(schemas);

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission Denied");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/get/{schemaName}")
    public ResponseEntity<?> findSchemaBySchemaName(@PathVariable String schemaName) {
        return new ResponseEntity<>(schemaMappingService.findSchemaBySchemaName(schemaName), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get/uuid/{uuuidName}")
    public ResponseEntity<?> findSchemaByUUIDName(@PathVariable String uuidName) {
        return new ResponseEntity<>(schemaMappingService.findSchemaByUUIDName(uuidName), HttpStatus.ACCEPTED);
    }

}
