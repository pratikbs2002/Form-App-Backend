package com.argusoft.form.security.filters;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.argusoft.form.entity.User;
import com.argusoft.form.repository.UserRepository;
import com.argusoft.form.security.datasource_config.DataSourceManager;
import com.argusoft.form.security.datasource_config.DynamicDataSource;
import com.argusoft.form.security.datasource_config.UserContextHolder;
import com.argusoft.form.service.SchemaMappingService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DynamicConnectionFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private SchemaMappingService schemaMappingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getRequestURI().equals("/auth/register")
                    || request.getRequestURI().equals("/auth/login")
                    || request.getRequestURI().equals("/data3") || request.getRequestURI().contains("/api/schema/mg")) {
                filterChain.doFilter(request, response);
            } else {
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();

                    System.out.println("\n=================> Username: " + username);
                    System.out.println("Current Context : " + UserContextHolder.getString());
                    // System.out.println(dataSource.getClass().getName());
                    System.out.println("\n Default Datasource: " + dynamicDataSource.getResolvedDefaultDataSource());
                    System.out.println(
                            "In Filterchain All Datasource: " + dynamicDataSource.getResolvedDataSources() + "\n");

                    if (username != null) {
                        try {
                            User user = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                            String currentSchema = schemaMappingService.findSchemaByUUIDName(user.getSchemaName()).get()
                                    .getSchemaName();

                            UserContextHolder.set(username);
                            UserContextHolder.setSchema(currentSchema);

                            dataSourceManager.getDataSource(user.getSchemaName(), user.getUsername(),
                                    user.getPassword());

                            System.out.println(
                                    "\nIn Filterchain All Datasource: " + dynamicDataSource.getResolvedDataSources()
                                            + "\n");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                        }
                    }

                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username header is required.");
                }
            }
        } finally {
            UserContextHolder.clear();
        }
    }
}