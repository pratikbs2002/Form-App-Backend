package com.argusoft.form.components;

import java.util.List;

import org.springframework.stereotype.Component;

import com.argusoft.form.service.CreateAndMigrateService;
import com.argusoft.form.service.FetchSchemaService;

import jakarta.annotation.PostConstruct;

@Component
public class StartupRunner {

    private final FetchSchemaService fetchSchemaService;
    private final CreateAndMigrateService createAndMigrateService;

    public StartupRunner(FetchSchemaService fetchSchemaService, CreateAndMigrateService createAndMigrateService) {
        this.createAndMigrateService = createAndMigrateService;
        this.fetchSchemaService = fetchSchemaService;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("Application has started!");
            List<String> schemas = getSchemas();

            for (int i = 1; i < schemas.size(); i++) {
                try {
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

    public List<String> getSchemas() {
        return fetchSchemaService.getSchemas();
    }

}
