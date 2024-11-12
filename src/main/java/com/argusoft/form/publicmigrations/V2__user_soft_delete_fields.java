package com.argusoft.form.publicmigrations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2__user_soft_delete_fields extends BaseJavaMigration {

        @Override
        public void migrate(Context context) throws Exception {
                Connection connection = context.getConnection();

                String addSoftDeleteColumn = "ALTER TABLE public.\"user\" "
                                + "ADD COLUMN deleted BOOLEAN DEFAULT FALSE, "
                                + "ADD COLUMN deleted_at TIMESTAMP;";

                try (PreparedStatement stmt = connection.prepareStatement(addSoftDeleteColumn)) {
                        stmt.executeUpdate();
                } catch (Exception e) {
                        System.out.println("Error executing SQL: " + e.getMessage());
                        throw e;
                }
                System.out.println("in public migration...");
        }
}
