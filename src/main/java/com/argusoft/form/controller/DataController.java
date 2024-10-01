package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/data3")
    public ResponseEntity<?> getData3() throws SQLException {
        return ResponseEntity.ok("Data without authentication");
    }

    @GetMapping("/data2")
    public ResponseEntity<?> getData2() throws SQLException {
        return ResponseEntity.ok("Data with authentication");
    }

    @GetMapping("/data")
    public ResponseEntity<?> getData() throws SQLException {
        List<Object> results = new ArrayList<>();
        System.out.println(dataSource.getConnection().getClientInfo("username"));

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Datasource class: " + dataSource.getClass().getName());
            System.out.println("Connection URL: " + connection.getMetaData().getURL());
            System.out.println("Database Product Name: " + connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try (Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM form")) {

            System.out.println(connection.getClientInfo("username"));
            while (rs.next()) {
                results.add(rs.getObject(1));
            }
            System.out.println("Data: " + results);

            return ResponseEntity.ok(results);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching data.");
        }
    }

}
