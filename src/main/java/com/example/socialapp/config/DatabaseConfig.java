package com.example.socialapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.socialapp.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // Spring Boot auto-configuration handles most of the database setup
    // This class is mainly for enabling JPA features and transaction management
}