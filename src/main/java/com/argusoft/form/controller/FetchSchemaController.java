package com.argusoft.form.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.repository.UserRepository;
import com.argusoft.form.service.FetchSchemaService;

@RestController
public class FetchSchemaController {

    private final FetchSchemaService fetchSchemaService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FetchSchemaController(FetchSchemaService fetchSchemaService, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.fetchSchemaService = fetchSchemaService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("schema")
    public List<String> getSchemas() {
        return fetchSchemaService.getSchemas();
    }

}
