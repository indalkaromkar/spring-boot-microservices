package com.programmingtechie.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order line item details")
public class OrderLineItemsDto {
    @Schema(description = "Line item ID", example = "1")
    private Long id;
    
    @Schema(description = "Product SKU code", example = "iphone-13")
    private String skuCode;
    
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;
    
    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;
}
