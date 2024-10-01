package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.User;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        String createUserQuery = "CREATE USER " + user.getUsername() + " WITH PASSWORD '"
                + passwordEncoder.encode(user.getPassword()) + "'";
        String grantUsageQuery = "GRANT USAGE ON SCHEMA " + user.getSchemaName() + " TO " + user.getUsername();
        String grantPrivilegesQuery = "GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA "
                + user.getSchemaName() + " TO " + user.getUsername();
        String setDefaultPrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                + " GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "
                + user.getUsername();

        try (Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createUserQuery);
            stmt.executeUpdate(grantUsageQuery);
            stmt.executeUpdate(grantPrivilegesQuery);
            stmt.executeUpdate(setDefaultPrivilegesQuery);

        } catch (SQLException e) {
            return ResponseEntity.ok("Something Wrong!");
        }

        userService.registerNewUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        System.out.println("+++++++++++++++++++++++++++++" + user);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword()));
            System.out.println("*************************************************************************");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("*************************************************************************+++");

            System.out.println(SecurityContextHolder.getContext().getAuthentication());

        } catch (AuthenticationException e) {
            System.out.println(e);
        }
        return ResponseEntity.ok("Login successful");
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
