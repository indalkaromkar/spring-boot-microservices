package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InventoryService inventoryService;

    @Test
    void shouldCheckInventoryStock() throws Exception {
        List<InventoryResponse> responses = List.of(
            createInventoryResponse("SKU001", true),
            createInventoryResponse("SKU002", false)
        );
        when(inventoryService.isInStock(any(List.class))).thenReturn(responses);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "SKU001", "SKU002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].skuCode").value("SKU001"))
                .andExpect(jsonPath("$[0].inStock").value(true))
                .andExpect(jsonPath("$[1].skuCode").value("SKU002"))
                .andExpect(jsonPath("$[1].inStock").value(false));
        
        verify(inventoryService, times(1)).isInStock(any(List.class));
    }

    @Test
    void shouldHandleEmptySkuList() throws Exception {
        when(inventoryService.isInStock(any(List.class))).thenReturn(List.of());
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldHandleSingleSku() throws Exception {
        List<InventoryResponse> responses = List.of(createInventoryResponse("SKU001", true));
        when(inventoryService.isInStock(any(List.class))).thenReturn(responses);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .param("skuCode", "SKU001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].skuCode").value("SKU001"))
                .andExpect(jsonPath("$[0].inStock").value(true));
    }

    private InventoryResponse createInventoryResponse(String skuCode, boolean inStock) {
        return InventoryResponse.builder()
                .skuCode(skuCode)
                .isInStock(inStock)
                .build();
    }
}