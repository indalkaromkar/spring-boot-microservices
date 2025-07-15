package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for product management operations.
 * Contains business logic for creating and retrieving products.
 * Handles data transformation between DTOs and entities.
 */
@Service // Marks this class as a Spring service component
public class ProductService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductService.class);
    // Repository for database operations on Product entities
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a new product and saves it to the database.
     * Transforms ProductRequest DTO to Product entity using builder pattern.
     * 
     * @param productRequest DTO containing product details from client request
     */
    public void createProduct(ProductRequest productRequest) {
        // Build Product entity from request DTO using builder pattern
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // Persist product to database via JPA repository
        productRepository.save(product);
        // Log successful product creation with generated ID
        log.info("Product {} is saved", product.getId());
    }

    /**
     * Retrieves all products from the database.
     * Transforms Product entities to ProductResponse DTOs for client consumption.
     * 
     * @return List of ProductResponse DTOs containing all product information
     */
    public List<ProductResponse> getAllProducts() {
        // Fetch all products from database
        List<Product> products = productRepository.findAll();

        // Transform entities to response DTOs using stream API and method reference
        return products.stream().map(this::mapToProductResponse).toList();
    }

    /**
     * Private helper method to transform Product entity to ProductResponse DTO.
     * Encapsulates the mapping logic for reusability and maintainability.
     * 
     * @param product Product entity from database
     * @return ProductResponse DTO for client response
     */
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
