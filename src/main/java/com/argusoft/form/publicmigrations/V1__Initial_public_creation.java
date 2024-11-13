package com.argusoft.form.publicmigrations;

import java.sql.Connection;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V1__Initial_public_creation extends BaseJavaMigration {

        @Override
        public void migrate(Context context) throws Exception {

                String adminPassword = new BCryptPasswordEncoder().encode("admin");
                String addRootUser = "CREATE USER global_admin WITH SUPERUSER PASSWORD '" + adminPassword + "'";
                String createSchemaMappingTable = "CREATE TABLE schema_mapping_table ("
                                + "uuid_name VARCHAR(255) PRIMARY KEY, "
                                + "schema_name VARCHAR(255) NOT NULL, "
                                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                                + ");";

                String createRoleTableSQL = "CREATE TABLE IF NOT EXISTS public.role ("
                                + "role_id SERIAL PRIMARY KEY, "
                                + "role_name VARCHAR(20) NOT NULL"
                                + ")";

                String insertRoleSQL = "INSERT INTO " + "public"
                                + ".role (role_name) VALUES ('GLOBAL_ADMIN'), ('ADMIN'), ('REPORTING_USER')";
                String createUserTable = "CREATE TABLE public.user ("
                                + "id SERIAL PRIMARY KEY, "
                                + "username VARCHAR(255) UNIQUE NOT NULL, "
                                + "password VARCHAR(255) NOT NULL, "
                                + "schema_name VARCHAR(255), "
                                + "role_id INT, "
                                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                                + "FOREIGN KEY (role_id) REFERENCES public.role(role_id)"
                                + ");";

                String createDatasourceTable = "CREATE TABLE public.datasource ("
                                + "id SERIAL PRIMARY KEY, "
                                + "username VARCHAR(255) UNIQUE NOT NULL, "
                                + "password VARCHAR(255) NOT NULL, "
                                + "schema_name VARCHAR(255), "
                                + "role VARCHAR(255), "
                                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                                + ");";

                String insertRootDatasource = "INSERT INTO public.datasource (password, schema_name, username, role) VALUES ('"
                                + adminPassword + "', 'public', 'global_admin', 'GLOBAL_ADMIN')";

                String insertSchemaName = "INSERT INTO public.schema_mapping_table (uuid_name, created_at, schema_name) VALUES ('public', NOW(), 'public')";
                String insertRootUser = "INSERT INTO public.user (password, schema_name, username, role_id) VALUES ('"
                                + adminPassword + "', 'public', 'global_admin', 1)";
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

                String addUserToSchemaFunction = """
                                CREATE OR REPLACE FUNCTION add_user_to_schema_table()
                                RETURNS TRIGGER AS $$
                                BEGIN
                                    IF NEW.schema_name != 'public' THEN
                                        EXECUTE format('INSERT INTO %I.users (id,fname,role_id) VALUES ($1,''temp'',$2)', NEW.schema_name)
                                        USING NEW.id, NEW.role_id;
                                    END IF;
                                    RETURN NEW;
                                END;
                                $$ LANGUAGE plpgsql;
                                """;

                String createUserTrigger = """
                                CREATE TRIGGER after_user_insert
                                AFTER INSERT ON public.user
                                FOR EACH ROW
                                EXECUTE FUNCTION add_user_to_schema_table();
                                """;
                try {
                        Connection connection = context.getConnection();
                        Statement stmt = connection.createStatement();

                        stmt.execute(addRootUser);
                        stmt.execute(createSchemaMappingTable);
                        stmt.execute(createRoleTableSQL);
                        stmt.execute(insertRoleSQL);
                        stmt.execute(createUserTable);
                        stmt.execute(insertSchemaName);
                        stmt.execute(insertRootUser);
                        stmt.execute(getAllSchema);
                        stmt.execute(createDatasourceTable);
                        stmt.execute(insertRootDatasource);
                        stmt.execute(addUserToSchemaFunction);
                        stmt.execute(createUserTrigger);

                        System.out.println("Public Migration Applied successfully.");

                } catch (Exception e) {
                        System.out.println("Error executing SQL: " + e.getMessage());
                        throw e;
                }
                System.out.println("in public migration...");
        }
}
