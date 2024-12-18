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

import com.argusoft.form.entity.Address;

@RestController
public class DataController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/data")
    public ResponseEntity<?> getData() throws SQLException {
        List<Address> results = new ArrayList<>();
        System.out.println(dataSource.getConnection().getClientInfo("username"));

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Datasource class: " + dataSource.getClass().getName());
            System.out.println("Connection URL: " + connection.getMetaData().getURL());
            System.out.println("Database User Name: " + connection.getMetaData().getUserName());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // try (Connection connection = dataSource.getConnection();
        // Statement stmt = connection.createStatement();
        // ResultSet rs = stmt.executeQuery("SELECT * FROM
        // _09cde47d_1246_47d8_b880_712ae0500cf5.address")) {
        // while (rs.next()) {
        // results.add(new Address(rs.getInt(1), rs.getString(2), rs.getString(3),
        // rs.getString(4), rs.getInt(5)));
        // }
        // System.out.println("Data: " + results);

        // return ResponseEntity.ok(results);

        // } catch (SQLException ex) {
        // System.out.println(ex.getMessage());
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body("An error occurred while fetching data.");
        // }
        return ResponseEntity.ok("d");
    }

}
