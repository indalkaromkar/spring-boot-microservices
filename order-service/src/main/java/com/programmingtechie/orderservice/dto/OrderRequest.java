package com.programmingtechie.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order creation request")
public class OrderRequest {
    @Schema(description = "List of order line items")
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
