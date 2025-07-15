package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void shouldReturnInStockForAvailableProducts() {
        List<Inventory> inventories = List.of(
            createInventory("SKU001", 10),
            createInventory("SKU002", 5)
        );
        when(inventoryRepository.findBySkuCodeIn(any(List.class))).thenReturn(inventories);
        
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("SKU001", "SKU002"));
        
        assertEquals(2, responses.size());
        assertTrue(responses.get(0).isInStock());
        assertTrue(responses.get(1).isInStock());
        verify(inventoryRepository, times(1)).findBySkuCodeIn(any(List.class));
    }

    @Test
    void shouldReturnOutOfStockForZeroQuantity() {
        List<Inventory> inventories = List.of(createInventory("SKU001", 0));
        when(inventoryRepository.findBySkuCodeIn(any(List.class))).thenReturn(inventories);
        
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("SKU001"));
        
        assertEquals(1, responses.size());
        assertFalse(responses.get(0).isInStock());
        assertEquals("SKU001", responses.get(0).getSkuCode());
    }

    @Test
    void shouldHandleEmptyInventoryList() {
        when(inventoryRepository.findBySkuCodeIn(any(List.class))).thenReturn(List.of());
        
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("NON_EXISTENT"));
        
        assertEquals(0, responses.size());
    }

    @Test
    void shouldHandleMixedStockLevels() {
        List<Inventory> inventories = List.of(
            createInventory("SKU001", 10),
            createInventory("SKU002", 0),
            createInventory("SKU003", 1)
        );
        when(inventoryRepository.findBySkuCodeIn(any(List.class))).thenReturn(inventories);
        
        List<InventoryResponse> responses = inventoryService.isInStock(List.of("SKU001", "SKU002", "SKU003"));
        
        assertEquals(3, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.getSkuCode().equals("SKU001") && r.isInStock()));
        assertTrue(responses.stream().anyMatch(r -> r.getSkuCode().equals("SKU002") && !r.isInStock()));
        assertTrue(responses.stream().anyMatch(r -> r.getSkuCode().equals("SKU003") && r.isInStock()));
    }

    private Inventory createInventory(String skuCode, Integer quantity) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(skuCode);
        inventory.setQuantity(quantity);
        return inventory;
    }
}