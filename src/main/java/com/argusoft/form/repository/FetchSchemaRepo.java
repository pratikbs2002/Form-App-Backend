package com.argusoft.form.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository
public class FetchSchemaRepo {

    private final DataSource dataSource;

    public FetchSchemaRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getSchemas() throws SQLException {
        List<String> data = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM get_all_schemas()")) {

            while (resultSet.next()) {
                String value = resultSet.getString(1);
                data.add(value);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching data: " + e.getMessage());
            throw e;
        }

        return data;
    }
}
