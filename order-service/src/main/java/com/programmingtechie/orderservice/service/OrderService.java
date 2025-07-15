package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.event.OrderPlacedEvent;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for order management operations.
 * Handles complex business logic including inventory validation, order persistence,
 * microservice communication, and event publishing for order processing workflow.
 */
@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates constructor for final fields (dependency injection)
@Transactional // Ensures all operations are executed within a database transaction
@Slf4j // Lombok annotation for logging support
public class OrderService {

    // Repository for database operations on Order entities
    private final OrderRepository orderRepository;
    // WebClient builder for making HTTP calls to other microservices
    private final WebClient.Builder webClientBuilder;
    // Registry for observability and monitoring of service calls
    private final ObservationRegistry observationRegistry;
    // Publisher for application events (order placed notifications)
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Places an order with comprehensive validation and processing.
     * Implements the complete order workflow:
     * 1. Create order with unique order number
     * 2. Transform DTOs to entities
     * 3. Validate inventory availability via microservice call
     * 4. Persist order if all products are in stock
     * 5. Publish order placed event for downstream processing
     * 
     * @param orderRequest DTO containing order details and line items
     * @return Success message if order is placed, throws exception if inventory unavailable
     */
    public String placeOrder(OrderRequest orderRequest) {
        // Create new order entity with unique identifier
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // Transform order line item DTOs to entities using stream API
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto) // Convert each DTO to entity
                .toList();

        // Associate line items with the order
        order.setOrderLineItemsList(orderLineItems);

        // Extract SKU codes for inventory validation
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Create observability context for monitoring inventory service calls
        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        
        // Execute inventory validation within observation context for monitoring
        return inventoryServiceObservation.observe(() -> {
            // Make synchronous HTTP call to inventory service to check stock availability
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block(); // Block to make synchronous call (consider async for better performance)

            // Validate that all requested products are available in inventory
            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                // Persist order to database if inventory is available
                orderRepository.save(order);
                // Publish domain event for downstream processing (notifications, analytics, etc.)
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                // Throw business exception if any product is out of stock
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        });
    }

    /**
     * Private helper method to transform OrderLineItemsDto to OrderLineItems entity.
     * Encapsulates the mapping logic for maintainability and reusability.
     * 
     * @param orderLineItemsDto DTO from client request
     * @return OrderLineItems entity for database persistence
     */
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
