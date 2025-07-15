package com.programmingtechie.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Product Service microservice.
 * This service handles product catalog management including creating and retrieving products.
 * Runs on port 8080 by default.
 */
@SpringBootApplication // Enables auto-configuration, component scanning, and configuration properties
public class ProductServiceApplication {

    /**
     * Main method to bootstrap the Spring Boot application.
     * Initializes the embedded web server and Spring application context.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
