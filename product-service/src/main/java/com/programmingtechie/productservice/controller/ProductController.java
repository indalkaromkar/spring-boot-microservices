package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing product operations.
 * Provides endpoints for creating and retrieving products in the e-commerce system.
 * All endpoints are prefixed with /api/product.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests
@RequestMapping("/api/product") // Base URL mapping for all endpoints in this controller
@Tag(name = "Product", description = "Product management APIs") // OpenAPI documentation tag
public class ProductController {

    // Dependency injection of ProductService to handle business logic
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product in the system.
     * Accepts product details via JSON request body and persists to database.
     * 
     * @param productRequest DTO containing product name, description, and price
     * @return HTTP 201 Created status on successful creation
     */
    @PostMapping // Maps HTTP POST requests to this method
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 status code on success
    @Operation(summary = "Create a new product", description = "Creates a new product in the system")
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    /**
     * Retrieves all products from the system.
     * Returns a list of all products with their details.
     * 
     * @return List of ProductResponse DTOs containing product information
     */
    @GetMapping // Maps HTTP GET requests to this method
    @ResponseStatus(HttpStatus.OK) // Returns 200 status code on success
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

}
