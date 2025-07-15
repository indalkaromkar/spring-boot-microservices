package com.programmingtechie.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Inventory Service microservice.
 * This service manages stock levels and provides inventory availability checks
 * for other services in the microservices ecosystem. Runs on port 8082 by default.
 */
@SpringBootApplication // Enables auto-configuration, component scanning, and configuration properties
public class InventoryServiceApplication {

    /**
     * Main method to bootstrap the Spring Boot application.
     * Initializes the embedded web server, Spring application context, and inventory management components.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
