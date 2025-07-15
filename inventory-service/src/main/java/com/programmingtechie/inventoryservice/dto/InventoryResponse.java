package com.programmingtechie.inventoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Inventory stock response")
public class InventoryResponse {
    @Schema(description = "Product SKU code", example = "iphone-13")
    private String skuCode;
    
    @Schema(description = "Stock availability status", example = "true")
    private boolean isInStock;
}
