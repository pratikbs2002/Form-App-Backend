package com.argusoft.form.security.datasource_config;


import org.springframework.util.Assert;

public class UserContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

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
}