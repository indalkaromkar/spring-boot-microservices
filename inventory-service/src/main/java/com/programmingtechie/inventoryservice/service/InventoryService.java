package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for inventory management operations.
 * Handles business logic for checking product availability and stock levels.
 * Provides efficient bulk inventory checking for microservice communication.
 */
@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates constructor for final fields (dependency injection)
@Slf4j // Lombok annotation for logging support
public class InventoryService {

    // Repository for database operations on Inventory entities
    private final InventoryRepository inventoryRepository;

    /**
     * Checks inventory availability for multiple products efficiently.
     * Uses a single database query to fetch all requested SKUs and determines
     * stock availability based on quantity greater than zero.
     * 
     * @param skuCode List of SKU codes to check for availability
     * @return List of InventoryResponse DTOs with stock status for each SKU
     */
    @Transactional(readOnly = true) // Read-only transaction for better performance and consistency
    @SneakyThrows // Lombok annotation to handle checked exceptions (if any)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory");
        
        // Fetch inventory records for all requested SKU codes in a single query
        // Transform entities to response DTOs using stream API and builder pattern
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                // Determine stock availability: quantity > 0 means in stock
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
