package com.programmingtechie.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Order Service microservice.
 * This service handles order processing, inventory validation, and event publishing.
 * Runs on port 8081 by default and communicates with Inventory Service.
 */
@SpringBootApplication // Enables auto-configuration, component scanning, and configuration properties
public class OrderServiceApplication {

    /**
     * Main method to bootstrap the Spring Boot application.
     * Initializes the embedded web server, Spring application context, and microservice components.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
