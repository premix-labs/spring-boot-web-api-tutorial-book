package com.example.secureadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SecureAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureAdminApplication.class, args);
    }
}
