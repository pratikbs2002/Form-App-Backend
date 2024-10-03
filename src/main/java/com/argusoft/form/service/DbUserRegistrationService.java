package com.argusoft.form.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.User;

@Service
public class DbUserRegistrationService {

    @Autowired
    private DataSource dataSource;

    public String registerDbUser(User user) throws SQLException {
        // Query to create a new database user
        String createUserQuery = "CREATE USER " + user.getUsername() + " WITH PASSWORD '"
                + user.getPassword() + "'";

        // Query to Grant Usage of schema to the database user
        String grantUsageQuery = "GRANT USAGE ON SCHEMA " + user.getSchemaName() + " TO " + user.getUsername();

        // Query to Grant privileges on existing tables to created user
        String grantPrivilegesQuery = "GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA "
                + user.getSchemaName() + " TO " + user.getUsername();

        // Query to grant default privileges for future tables
        String setDefaultPrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                + " GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "
                + user.getUsername();

        // Create connection using datasource to execute above queries
        try (Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createUserQuery);
            stmt.executeUpdate(grantUsageQuery);
            stmt.executeUpdate(grantPrivilegesQuery);
            stmt.executeUpdate(setDefaultPrivilegesQuery);

        } catch (SQLException e) {
            throw e;
        }
        return "Database User created Successfully";
    }

}
