package com.argusoft.form.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.Schema;
import com.argusoft.form.service.SchemaMappingService;

@RestController
@RequestMapping("schema")
public class SchemaMappingController {

    private SchemaMappingService schemaMappingService;

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

    @GetMapping("/get/{schemaName}")
    public ResponseEntity<?> findSchemaBySchemaName(@PathVariable String schemaName) {
        return new ResponseEntity<>(schemaMappingService.findSchemaBySchemaName(schemaName), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get/uuid/{uuuidName}")
    public ResponseEntity<?> findSchemaByUUIDName(@PathVariable String uuidName) {
        return new ResponseEntity<>(schemaMappingService.findSchemaByUUIDName(uuidName), HttpStatus.ACCEPTED);
    }

}
