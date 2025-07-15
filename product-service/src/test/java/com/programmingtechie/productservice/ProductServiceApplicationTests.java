package com.programmingtechie.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import com.programmingtechie.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = createProductRequest("iPhone 13", "Apple iPhone", BigDecimal.valueOf(999));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        assertEquals(1, productRepository.findAll().size());
        Product saved = productRepository.findAll().get(0);
        assertEquals("iPhone 13", saved.getName());
        assertEquals("Apple iPhone", saved.getDescription());
        assertEquals(BigDecimal.valueOf(999), saved.getPrice());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        productService.createProduct(createProductRequest("iPhone 13", "Apple iPhone", BigDecimal.valueOf(999)));
        productService.createProduct(createProductRequest("Samsung S21", "Samsung Galaxy", BigDecimal.valueOf(799)));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("iPhone 13"))
                .andExpect(jsonPath("$[1].name").value("Samsung S21"));
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldCreateProductWithValidData() {
        ProductRequest request = createProductRequest("Test Product", "Test Description", BigDecimal.valueOf(100));
        
        productService.createProduct(request);
        
        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void shouldGetAllProductsFromService() {
        productService.createProduct(createProductRequest("Product 1", "Desc 1", BigDecimal.valueOf(50)));
        productService.createProduct(createProductRequest("Product 2", "Desc 2", BigDecimal.valueOf(75)));
        
        List<ProductResponse> responses = productService.getAllProducts();
        
        assertEquals(2, responses.size());
        assertEquals("Product 1", responses.get(0).getName());
        assertEquals("Product 2", responses.get(1).getName());
    }

    @Test
    void shouldHandleInvalidJsonRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateProductWithZeroPrice() throws Exception {
        ProductRequest request = createProductRequest("Free Product", "Free item", BigDecimal.ZERO);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        assertEquals(1, productRepository.findAll().size());
    }

    private ProductRequest createProductRequest(String name, String description, BigDecimal price) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        return request;
    }
}