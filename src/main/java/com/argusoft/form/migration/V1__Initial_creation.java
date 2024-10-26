package com.argusoft.form.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1__Initial_creation extends BaseJavaMigration {

        @Override
        public void migrate(Context context) throws Exception {
                String schema = context.getConfiguration().getSchemas()[0];
                String createRoleTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".role ("
                                + "role_id SERIAL PRIMARY KEY, "
                                + "role_name VARCHAR(20) NOT NULL"
                                + ")";

                String insertRoleSQL = "INSERT INTO " + schema
                                + ".role (role_name) VALUES ('admin'), ('reporting user')";

                String createAddressTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".address ("
                                + "id SERIAL PRIMARY KEY, "
                                + "add_line VARCHAR(255) NOT NULL, "
                                + "city VARCHAR(100) NOT NULL, "
                                + "state VARCHAR(100), "
                                + "pincode INT NOT NULL, "
                                + "CONSTRAINT invalid_pincode CHECK (pincode BETWEEN 100000 AND 999999)"
                                + ")";

                String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".users ("
                                + "id SERIAL PRIMARY KEY, "
                                + "fname VARCHAR(50) NOT NULL, "
                                + "lname VARCHAR(50), "
                                + "role_id INT NOT NULL, "
                                + "address_id INT, "
                                + "FOREIGN KEY (role_id) REFERENCES " + schema + ".role(role_id), "
                                + "FOREIGN KEY (address_id) REFERENCES " + schema + ".address(id)"
                                + ")";

                String createCreateFormTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".create_form ("
                                + "id SERIAL PRIMARY KEY, "
                                + "title VARCHAR(255), "
                                + "admin_id INT NOT NULL, "
                                + "questions JSONB, "
                                + "created_at TIMESTAMP DEFAULT NOW(), "
                                + "FOREIGN KEY (admin_id) REFERENCES " + schema + ".users(id)"
                                + ")";

                String createLocationsTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".locations ("
                                + "id SERIAL PRIMARY KEY, "
                                + "name VARCHAR(255) NOT NULL, "
                                + "parent_id INT REFERENCES " + schema + ".locations(id) ON DELETE CASCADE"
                                + ")";

                String createLocType = "CREATE TYPE " + schema + ".loc AS (lat float, long float)";

                String createFillFormTableSQL = "CREATE TABLE IF NOT EXISTS " + schema + ".fill_form ("
                                + "id SERIAL PRIMARY KEY, "
                                + "form_id INT NOT NULL, "
                                + "user_id INT NOT NULL, "
                                + "title VARCHAR(255), "
                                + "answers JSONB, "
                                + "created_at TIMESTAMP DEFAULT NOW(), "
                                + "location loc NOT NULL, "
                                + "location_id INT NOT NULL, "
                                + "FOREIGN KEY (form_id) REFERENCES " + schema + ".create_form(id), "
                                + "FOREIGN KEY (user_id) REFERENCES " + schema + ".users(id), "
                                + "FOREIGN KEY (location_id) REFERENCES " + schema + ".locations(id), "
                                + "UNIQUE (form_id, user_id)"
                                + ")";

                String insertRootLocationSQL = "INSERT INTO " + schema
                                + ".locations (name, parent_id) VALUES ('root',null)";

                Connection connection = null;
                Statement statement = null;

                try {
                        connection = context.getConnection();
                        statement = connection.createStatement();

                        statement.execute(createRoleTableSQL);
                        statement.execute(insertRoleSQL);
                        statement.execute(createAddressTableSQL);
                        statement.execute(createUsersTableSQL);
                        statement.execute(createCreateFormTableSQL);
                        statement.execute(createLocationsTableSQL);
                        statement.execute(createLocType);
                        statement.execute(createFillFormTableSQL);
                        statement.execute(insertRootLocationSQL);

                        System.out.println("Tables created successfully.");
                } catch (SQLException e) {
                        System.out.println("Error executing SQL: " + e.getMessage());
                        throw e;
                } finally {
                        // if (statement != null) {
                        // try {
                        // statement.close();
                        // } catch (SQLException e) {
                        // System.out.println("Error closing statement: " + e.getMessage());
                        // }
                        // }
                        // if (connection != null) {
                        // try {
                        // connection.close();
                        // } catch (SQLException e) {
                        // System.out.println("Error closing statement: " + e.getMessage());
                        // }
                        // }
                }
        }
}
