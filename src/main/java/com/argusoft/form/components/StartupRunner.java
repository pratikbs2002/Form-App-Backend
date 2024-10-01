package com.argusoft.form.components;

import java.util.List;

import org.springframework.stereotype.Component;

import com.argusoft.form.service.CreateAndMigrateService;
import com.argusoft.form.service.FetchSchemaService;

import jakarta.annotation.PostConstruct;

/*
This is the startup file that runs first when the application starts. Here’s what it does:
It retrieves the list of schemas from the database using FetchSchemaService and applies any necessary migrations if they haven't been applied yet.
 */

@Component
public class StartupRunner {

    private final FetchSchemaService fetchSchemaService;
    private final CreateAndMigrateService createAndMigrateService;

    // Contructor Injection
    public StartupRunner(FetchSchemaService fetchSchemaService, CreateAndMigrateService createAndMigrateService) {
        this.createAndMigrateService = createAndMigrateService;
        this.fetchSchemaService = fetchSchemaService;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("Application has started!");

            // Call GetSchemas Method
            List<String> schemas = getSchemas();

            // Traverse the schemas list and Apply Migration using Migration Service
            for (int i = 1; i < schemas.size(); i++) {
                try {
                    System.out.println(schemas.get(i));
                    String result = createAndMigrateService.applyMigration(schemas.get(i));
                    System.out.println(schemas.get(i) + " : " + result);

                } catch (Exception e) {
                    System.out.println("Error : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method : To Get All Available Schemas in the Database
    public List<String> getSchemas() {
        return fetchSchemaService.getSchemas();
    }

}
