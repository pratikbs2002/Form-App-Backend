package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.User;
import com.argusoft.form.security.datasource_config.UserContextHolder;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.SchemaMappingService;
import com.argusoft.form.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final DbUserRegistrationService dbUserRegistrationService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    public UserController(UserService userService, DbUserRegistrationService dbUserRegistrationService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.dbUserRegistrationService = dbUserRegistrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public User getUserDetails(@PathVariable String username) {
        return userService.getUserConnectionDetails(username);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        System.out.println(userService.getAllUsers().toString());
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add/admin")
    public ResponseEntity<?> addAdminUser(@RequestBody Map<String, String> userData) {
        String schemaUUID;
        try (Connection connection = dataSource.getConnection()) {
            schemaUUID = connection.getSchema();
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        User user = new User();
        user.setSchemaName(schemaUUID);
        user.setUsername(userData.get("username") + "_" + UserContextHolder.getSchema() + "_admin");
        user.setPassword(passwordEncoder.encode(userData.get("password")));
        user.setRole("admin");
        System.out.println(user);

        // Create Database user
        try {
            dbUserRegistrationService.registerDbUser(user);
        } catch (SQLException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        // // Add Database user details in public schema user table
        try {
            UserContextHolder.set("public");
            System.out.println(UserContextHolder.getString());
            userService.registerNewUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return ResponseEntity.ok(user);
    }
}
