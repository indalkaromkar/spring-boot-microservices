package com.programmingtechie.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.service.OrderService;
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

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    @Test
    void shouldPlaceOrder() throws Exception {
        OrderRequest request = createOrderRequest();
        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn("Order Placed");
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        verify(orderService, times(1)).placeOrder(any(OrderRequest.class));
    }

    @Test
    void shouldHandleFallback() throws Exception {
        OrderRequest request = createOrderRequest();
        when(orderService.placeOrder(any(OrderRequest.class))).thenThrow(new RuntimeException("Service down"));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldHandleEmptyOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderLineItemsDtoList(List.of());
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private OrderRequest createOrderRequest() {
        OrderRequest request = new OrderRequest();
        OrderLineItemsDto item = new OrderLineItemsDto();
        item.setSkuCode("SKU001");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(1);
        request.setOrderLineItemsDtoList(List.of(item));
        return request;
    }
}