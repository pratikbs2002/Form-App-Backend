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

        public String registerAdminDbUser(User user) throws SQLException {
                // Query to create a new database user
                String createUserQuery = "CREATE USER " + user.getUsername() + " WITH PASSWORD '"
                                + user.getPassword() + "' CREATEROLE    ";

                // Query to Grant Usage of schema to the database user
                String grantSchemaUsageQuery = "GRANT USAGE ON SCHEMA " + user.getSchemaName() + " TO "
                                + user.getUsername()
                                + " WITH GRANT OPTION";

                // Query to Grant privileges on existing tables to created user
                String grantTablePrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername() + " WITH GRANT OPTION";

                // Query to grant default privileges for future tables
                String setDefaultTablePrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON TABLES TO "
                                + user.getUsername() + " WITH GRANT OPTION";

                // Query to Grant privileges on existing sequences to created user
                String grantSequencePrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername() + " WITH GRANT OPTION";

                // Query to grant default privileges for future sequences
                String setDefaultSequencePrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON SEQUENCES TO "
                                + user.getUsername() + " WITH GRANT OPTION";

                // Query to Grant privileges on existing functions to created user
                String grantFuntionPrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername() + " WITH GRANT OPTION";

                // Query to grant default privileges for future functions
                String setDefaultFunctionPrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON FUNCTIONS TO "
                                + user.getUsername() + " WITH GRANT OPTION";

                // Grant Public Schema to reportinguser role
                String grantPublicSchemaToAdminRole = "GRANT USAGE ON SCHEMA public TO " + user.getUsername();

                // Grant Public Schema User Table to reportinguser role
                String grantPublicUserTableToAdminRole = "GRANT SELECT, INSERT, UPDATE ON public.user TO "
                                + user.getUsername();

                // Grant Public Schema Role Table to reportinguser role
                String grantPublicRoleTableToAdminRole = "GRANT SELECT, INSERT, UPDATE ON public.role TO "
                                + user.getUsername();

                // Grant USAGE and SELECT on the sequence associated with the user table
                String grantPublicUserSequenceToAdminRole = "GRANT USAGE, SELECT ON SEQUENCE public.user_id_seq TO "
                                + user.getUsername();

                // Create connection using datasource to execute above queries
                try (Connection connection = dataSource.getConnection();
                                Statement stmt = connection.createStatement()) {

                        stmt.executeUpdate(createUserQuery);
                        stmt.executeUpdate(grantSchemaUsageQuery);
                        stmt.executeUpdate(grantTablePrivilegesQuery);
                        stmt.executeUpdate(setDefaultTablePrivilegesQuery);
                        stmt.executeUpdate(grantSequencePrivilegesQuery);
                        stmt.executeUpdate(setDefaultSequencePrivilegesQuery);
                        stmt.executeUpdate(grantFuntionPrivilegesQuery);
                        stmt.executeUpdate(setDefaultFunctionPrivilegesQuery);
                        stmt.executeUpdate(grantPublicSchemaToAdminRole);
                        stmt.executeUpdate(grantPublicUserTableToAdminRole);
                        stmt.executeUpdate(grantPublicRoleTableToAdminRole);
                        stmt.executeUpdate(grantPublicUserSequenceToAdminRole);

                } catch (SQLException e) {
                        throw e;
                }
                return "Database User created Successfully";
        }

        public String registerReportingDbUser(User user) throws SQLException {
                // Query to create a new database user
                String createUserQuery = "CREATE USER " + user.getUsername() + " WITH PASSWORD '"
                                + user.getPassword() + "'";

                // Query to Grant Usage of schema to the database user
                String grantSchemaUsageQuery = "GRANT USAGE ON SCHEMA " + user.getSchemaName() + " TO "
                                + user.getUsername();

                // Query to Grant privileges on existing tables to created user
                String grantTablePrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername();

                // Query to grant default privileges for future tables
                String setDefaultTablePrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON TABLES TO "
                                + user.getUsername();

                // Query to Grant privileges on existing sequences to created user
                String grantSequencePrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername();

                // Query to grant default privileges for future sequences
                String setDefaultSequencePrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON SEQUENCES TO "
                                + user.getUsername();

                // Query to Grant privileges on existing functions to created user
                String grantFuntionPrivilegesQuery = "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA "
                                + user.getSchemaName() + " TO " + user.getUsername();

                // Query to grant default privileges for future functions
                String setDefaultFunctionPrivilegesQuery = "ALTER DEFAULT PRIVILEGES IN SCHEMA " + user.getSchemaName()
                                + " GRANT ALL PRIVILEGES ON FUNCTIONS TO "
                                + user.getUsername();

                // Grant Public Schema to Reporting role
                String grantPublicSchemaToAdminRole = "GRANT USAGE ON SCHEMA public TO " + user.getUsername();

                // Grant Public Schema User Table to reportinguser role
                String grantPublicUserTableToAdminRole = "GRANT SELECT, INSERT, UPDATE ON public.user TO "
                                + user.getUsername();

                // Grant Public Schema Role Table to reportinguser role
                String grantPublicRoleTableToAdminRole = "GRANT SELECT, INSERT, UPDATE ON public.role TO "
                                + user.getUsername();

                // Grant USAGE and SELECT on the sequence associated with the user table
                String grantPublicUserSequenceToAdminRole = "GRANT USAGE, SELECT ON SEQUENCE public.user_id_seq TO "
                                + user.getUsername();

                // Create connection using datasource to execute above queries
                try (Connection connection = dataSource.getConnection();
                                Statement stmt = connection.createStatement()) {

                        stmt.executeUpdate(createUserQuery);
                        stmt.executeUpdate(grantSchemaUsageQuery);
                        stmt.executeUpdate(grantTablePrivilegesQuery);
                        stmt.executeUpdate(setDefaultTablePrivilegesQuery);
                        stmt.executeUpdate(grantSequencePrivilegesQuery);
                        stmt.executeUpdate(setDefaultSequencePrivilegesQuery);
                        stmt.executeUpdate(grantFuntionPrivilegesQuery);
                        stmt.executeUpdate(setDefaultFunctionPrivilegesQuery);

                        stmt.executeUpdate(grantPublicSchemaToAdminRole);
                        stmt.executeUpdate(grantPublicUserTableToAdminRole);
                        stmt.executeUpdate(grantPublicRoleTableToAdminRole);
                        stmt.executeUpdate(grantPublicUserSequenceToAdminRole);

                } catch (SQLException e) {
                        throw e;
                }
                return "Database User created Successfully";
        }
}
