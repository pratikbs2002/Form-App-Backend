package com.argusoft.form.publicmigrations;

import java.sql.Connection;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2__intial_public_creation extends BaseJavaMigration {

        @Override
        public void migrate(Context context) throws Exception {

                String adminPassword = new BCryptPasswordEncoder().encode("admin");
                String addRootUser = "CREATE USER global_admin WITH SUPERUSER PASSWORD '" + adminPassword + "'";
                String insertSchemaName = "INSERT INTO public.schema_mapping_table (uuid_name, created_at, schema_name) VALUES ('public', NOW(), 'public')";
                String insertRootUser = "INSERT INTO public.user (password, schema_name, username) VALUES ('"
                                + adminPassword + "', 'public', 'global_admin')";
                String getAllSchema = """
                                CREATE OR REPLACE FUNCTION get_all_schemas()
                                RETURNS TABLE(schema_name TEXT) AS $$
                                BEGIN
                                    RETURN QUERY
                                    SELECT nspname::text
                                    FROM pg_catalog.pg_namespace
                                    WHERE nspname NOT IN ('pg_catalog', 'information_schema', 'pg_toast');
                                END;
                                $$ LANGUAGE plpgsql;
                                """;
                try {
                        Connection connection = context.getConnection();
                        Statement stmt = connection.createStatement();

                        stmt.execute(addRootUser);
                        stmt.execute(insertSchemaName);
                        stmt.execute(insertRootUser);
                        stmt.execute(getAllSchema);
                        System.out.println("Public Migration Applied successfully.");

                } catch (Exception e) {
                        System.out.println("Error executing SQL: " + e.getMessage());
                        throw e;
                }
                System.out.println("in public migration...");
        }
}
