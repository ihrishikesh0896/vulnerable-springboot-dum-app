package com.dummy;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    private String dbPassword = "s3cr3t-db-pass";

    public String getDbPassword() {
        return dbPassword;
    }
}
