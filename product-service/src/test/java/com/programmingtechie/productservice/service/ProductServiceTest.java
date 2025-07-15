package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        ProductRequest request = createProductRequest("iPhone 13", "Apple iPhone", BigDecimal.valueOf(999));
        Product savedProduct = createProduct("1", "iPhone 13", "Apple iPhone", BigDecimal.valueOf(999));
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        
        productService.createProduct(request);
        
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = List.of(
            createProduct("1", "iPhone 13", "Apple iPhone", BigDecimal.valueOf(999)),
            createProduct("2", "Samsung S21", "Samsung Galaxy", BigDecimal.valueOf(799))
        );
        when(productRepository.findAll()).thenReturn(products);
        
        List<ProductResponse> responses = productService.getAllProducts();
        
        assertEquals(2, responses.size());
        assertEquals("iPhone 13", responses.get(0).getName());
        assertEquals("Samsung S21", responses.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(List.of());
        
        List<ProductResponse> responses = productService.getAllProducts();
        
        assertEquals(0, responses.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldMapProductToResponse() {
        Product product = createProduct("1", "Test Product", "Test Description", BigDecimal.valueOf(100));
        when(productRepository.findAll()).thenReturn(List.of(product));
        
        List<ProductResponse> responses = productService.getAllProducts();
        
        assertEquals(1, responses.size());
        ProductResponse response = responses.get(0);
        assertEquals("1", response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(BigDecimal.valueOf(100), response.getPrice());
    }

    private ProductRequest createProductRequest(String name, String description, BigDecimal price) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        return request;
    }

    private Product createProduct(String id, String name, String description, BigDecimal price) {
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }
}