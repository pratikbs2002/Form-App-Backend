package com.argusoft.form.controller;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.User;
import com.argusoft.form.service.CustomUserDetails;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DbUserRegistrationService dbUserRegistrationService;

    // Register API to register Database user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Get Password From the user object and reset it with applying password encoder
        // to encode it
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
            return ResponseEntity.ok(userDetails.getUser());

        } catch (AuthenticationException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear the SecurityContext to remove authentication details
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logout successful");
    }
}
