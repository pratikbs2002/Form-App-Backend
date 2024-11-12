package com.argusoft.form.controller;

import java.sql.SQLException;

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

import com.argusoft.form.dto.RoleDTO;
import com.argusoft.form.dto.UserDTO;
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
    private DbUserRegistrationService dbUserRegistrationService;

    // Register API to register Database user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Get Password From the user object and reset it with applying password encoder
        // to encode it
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create Database user
        try {
            dbUserRegistrationService.registerAdminDbUser(user);
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
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User authenticatedUser = userDetails.getUser();

            // Check if the user is soft-deleted
            if (authenticatedUser.isDeleted()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("This account has been deactivated. Please contact support.");
            }

            // Set authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create UserDTO for response
            UserDTO userDto = new UserDTO(
                    authenticatedUser.getId(),
                    authenticatedUser.getUsername(),
                    authenticatedUser.getSchemaName(),
                    new RoleDTO(authenticatedUser.getRole().getRoleId(), authenticatedUser.getRole().getRoleName()),
                    authenticatedUser.getCreated_at(),
                    authenticatedUser.isDeleted(),
                    authenticatedUser.getDeleted_at());

            // Return successful response
            return ResponseEntity.ok(userDto);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
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
