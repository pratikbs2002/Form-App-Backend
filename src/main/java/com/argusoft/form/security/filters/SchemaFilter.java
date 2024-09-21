package com.argusoft.form.security.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.argusoft.form.entity.User;
import com.argusoft.form.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SchemaFilter extends OncePerRequestFilter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;


    private AuthenticationManager authManager;

    public SchemaFilter(AuthenticationManager authenticationManager) {
        this.authManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(username);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
                connection.createStatement().execute("SET search_path TO " +
                        user.getSchemaName());
                System.out.println(user.getSchemaName());
            } catch (SQLException e) {
                throw new RuntimeException(user.getSchemaName() + "Failed to set schema: ",
                        e);
            }
        }

        filterChain.doFilter(request, response);
    }

}
