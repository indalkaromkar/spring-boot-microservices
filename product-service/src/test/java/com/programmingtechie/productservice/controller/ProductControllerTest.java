package com.programmingtechie.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = createProductRequest("iPhone 13", "Apple iPhone", BigDecimal.valueOf(999));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        List<ProductResponse> responses = List.of(
            createProductResponse("1", "iPhone 13", "Apple iPhone", BigDecimal.valueOf(999)),
            createProductResponse("2", "Samsung S21", "Samsung Galaxy", BigDecimal.valueOf(799))
        );
        when(productService.getAllProducts()).thenReturn(responses);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("iPhone 13"))
                .andExpect(jsonPath("$[1].name").value("Samsung S21"));
        
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private ProductRequest createProductRequest(String name, String description, BigDecimal price) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        return request;
    }

    private ProductResponse createProductResponse(String id, String name, String description, BigDecimal price) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }
}