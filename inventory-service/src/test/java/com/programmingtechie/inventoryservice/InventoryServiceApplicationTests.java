package com.programmingtechie.inventoryservice;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import com.programmingtechie.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
        setupTestData();
    }

    @Test
    void shouldReturnInStockForAvailableProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "iphone-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].skuCode").value("iphone-13"))
                .andExpect(jsonPath("$[0].inStock").value(true));
    }

    @Test
    void shouldReturnOutOfStockForUnavailableProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "out-of-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].skuCode").value("out-of-stock"))
                .andExpect(jsonPath("$[0].inStock").value(false));
    }

    @Test
    void shouldReturnMultipleInventoryItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "iphone-13", "samsung-s21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].skuCode").value("iphone-13"))
                .andExpect(jsonPath("$[0].inStock").value(true))
                .andExpect(jsonPath("$[1].skuCode").value("samsung-s21"))
                .andExpect(jsonPath("$[1].inStock").value(true));
    }

    @Test
    void shouldReturnEmptyListForNonExistentProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "non-existent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldCheckInventoryFromService() {
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("iphone-13", "samsung-s21"));
        
        assertEquals(2, responses.size());
        assertEquals("iphone-13", responses.get(0).getSkuCode());
        assertTrue(responses.get(0).isInStock());
        assertEquals("samsung-s21", responses.get(1).getSkuCode());
        assertTrue(responses.get(1).isInStock());
    }

    @Test
    void shouldReturnFalseForZeroQuantity() {
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("out-of-stock"));
        
        assertEquals(1, responses.size());
        assertEquals("out-of-stock", responses.get(0).getSkuCode());
        assertFalse(responses.get(0).isInStock());
    }

    @Test
    void shouldHandleEmptySkuCodeList() {
        List<InventoryResponse> responses = inventoryService.isInStock(List.of());
        
        assertEquals(0, responses.size());
    }

    @Test
    void shouldHandleMixedAvailability() {
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("iphone-13", "out-of-stock"));
        
        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.getSkuCode().equals("iphone-13") && r.isInStock()));
        assertTrue(responses.stream().anyMatch(r -> r.getSkuCode().equals("out-of-stock") && !r.isInStock()));
    }

    @Test
    void shouldReturnCorrectStockStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "iphone-13", "out-of-stock", "samsung-s21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.skuCode=='iphone-13')].inStock").value(true))
                .andExpect(jsonPath("$[?(@.skuCode=='out-of-stock')].inStock").value(false))
                .andExpect(jsonPath("$[?(@.skuCode=='samsung-s21')].inStock").value(true));
    }

    private void setupTestData() {
        inventoryRepository.saveAll(List.of(
            createInventory("iphone-13", 10),
            createInventory("samsung-s21", 5),
            createInventory("out-of-stock", 0)
        ));
    }

    private Inventory createInventory(String skuCode, Integer quantity) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(skuCode);
        inventory.setQuantity(quantity);
        return inventory;
    }
}