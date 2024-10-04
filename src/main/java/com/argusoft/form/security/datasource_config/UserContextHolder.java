package com.argusoft.form.security.datasource_config;

import org.springframework.util.Assert;

/*
 * Context Holder to hold current user or tenant
 */
public class UserContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();
    private static final ThreadLocal<String> schema = new ThreadLocal<>();

    public static void set(String String) {
        Assert.notNull(String, "String cannot be null");
        context.set(String);
    }

    public static String getString() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

    // for schema based on request
    public static void setSchema(String String) {
        schema.set(String);
    }

    public static String getSchema() {
        return schema.get();
    }

    public static void clearSchema() {
        schema.remove();
    }

}