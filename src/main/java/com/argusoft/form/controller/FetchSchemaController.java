package com.argusoft.form.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.repository.UserRepository;
import com.argusoft.form.service.FetchSchemaService;

@RestController
@CrossOrigin("*")
public class FetchSchemaController {

    private final FetchSchemaService fetchSchemaService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

    @Autowired
    public FetchSchemaController(FetchSchemaService fetchSchemaService, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.fetchSchemaService = fetchSchemaService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("schema")
    public List<String> getSchemas() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // System.out.println(user.getUsername());
        System.out.println("*******************************************");
        return fetchSchemaService.getSchemas();
    }

    @GetMapping("current-schema")
    public ResponseEntity<?> getCurrentSchema() throws SQLException {
        String schemaName = dataSource.getConnection().getSchema();
        System.out.println("Current Schema: " + schemaName);

        System.out.println("*******************************************");
        Map<String, String> mp = new HashMap<>();
        mp.put("schemaName", schemaName);
        return ResponseEntity.ok(mp);
    }

}
