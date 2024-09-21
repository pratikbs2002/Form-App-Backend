package com.argusoft.form.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FetchSchemaRepo {

    private final DataSource dataSource;

    @Autowired
    public FetchSchemaRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getSchemas() throws SQLException {

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();) {

            statement.execute("SELECT * FROM get_all_schemas()");
            ResultSet resultSet = statement.getResultSet();

            List<String> data = new ArrayList<>();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
                data.add(value);
            }
            // connection.close();
            return data;

        } catch (SQLException e) {
            System.out.println("Error while fetching data : " + e.getMessage());
        }

        return null;
    }
}
