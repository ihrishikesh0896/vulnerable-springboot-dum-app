package com.dummy;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    private String dbPassword = "uuid_5709d1ae234643ff9263d8564d0eeee6";

    public String getDbPassword() {
        return dbPassword;
    }
}
