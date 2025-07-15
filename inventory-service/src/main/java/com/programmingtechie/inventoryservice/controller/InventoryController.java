package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for inventory management operations.
 * Provides endpoints for checking product availability and stock levels.
 * Used by other microservices (especially Order Service) to validate inventory before processing orders.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests
@RequestMapping("/api/inventory") // Base URL mapping for all endpoints in this controller
@RequiredArgsConstructor // Lombok annotation to generate constructor for final fields
@Slf4j // Lombok annotation for logging support
@Tag(name = "Inventory", description = "Inventory management APIs") // OpenAPI documentation tag
public class InventoryController {

    // Dependency injection of InventoryService to handle business logic
    private final InventoryService inventoryService;

    /**
     * Checks inventory availability for multiple products by their SKU codes.
     * Supports bulk inventory checking for efficient order processing.
     * 
     * Example URLs:
     * - Single product: http://localhost:8082/api/inventory?skuCode=iphone-13
     * - Multiple products: http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
     * 
     * @param skuCode List of SKU codes to check for availability
     * @return List of InventoryResponse DTOs indicating stock status for each SKU
     */
    @GetMapping // Maps HTTP GET requests to this method
    @ResponseStatus(HttpStatus.OK) // Returns 200 status code on success
    @Operation(summary = "Check inventory stock", description = "Checks if products are in stock by SKU codes")
    public List<InventoryResponse> isInStock(
            @Parameter(description = "List of SKU codes to check", example = "[\"iphone-13\", \"iphone13-red\"]") 
            @RequestParam List<String> skuCode) {
        // Log incoming request for monitoring and debugging purposes
        log.info("Received inventory check request for skuCode: {}", skuCode);
        // Delegate to service layer for business logic execution
        return inventoryService.isInStock(skuCode);
    }
}

