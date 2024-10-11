package com.argusoft.form.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.User;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final DbUserRegistrationService dbUserRegistrationService;

    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/add/admin")
    public ResponseEntity<?> addAdminUser(@RequestBody User user) {
        // Get Password From the user object and reset it with applying password encoder
        // to encode it
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("admin");
        System.out.println(user);
        // Create Databas user
        try {
            dbUserRegistrationService.registerDbUser(user);
        } catch (SQLException e) {
            return ResponseEntity.ok(e.getMessage() + "Somthing Wrong!");
        }

        // Add Database user details in public schema user table
        userService.registerNewUser(user);
        return ResponseEntity.ok(user + "User registered successfully");

    }
}
