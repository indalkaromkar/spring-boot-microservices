package com.programmingtechie.orderservice.controller;

import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for managing order operations.
 * Implements resilience patterns (Circuit Breaker, Retry, Time Limiter) for fault tolerance.
 * Handles asynchronous order processing with inventory validation.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests
@RequestMapping("/api/order") // Base URL mapping for all endpoints in this controller
@RequiredArgsConstructor // Lombok annotation to generate constructor for final fields
@Slf4j // Lombok annotation for logging support
@Tag(name = "Order", description = "Order management APIs") // OpenAPI documentation tag
public class OrderController {

    // Dependency injection of OrderService to handle business logic
    private final OrderService orderService;

    /**
     * Places a new order with comprehensive resilience patterns.
     * Implements Circuit Breaker, Retry, and Time Limiter for fault tolerance.
     * Processes orders asynchronously to improve performance and user experience.
     * 
     * @param orderRequest DTO containing order details and line items
     * @return CompletableFuture with order confirmation message
     */
    @PostMapping // Maps HTTP POST requests to this method
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 status code on success
    @Operation(summary = "Place an order", description = "Places a new order with inventory validation")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod") // Circuit breaker for inventory service calls
    @TimeLimiter(name = "inventory") // Time limiter to prevent long-running requests
    @Retry(name = "inventory") // Retry mechanism for transient failures
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Placing Order");
        // Execute order placement asynchronously to avoid blocking the request thread
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    /**
     * Fallback method executed when circuit breaker is open or service calls fail.
     * Provides graceful degradation by returning a user-friendly error message.
     * Must have the same signature as the original method plus RuntimeException parameter.
     * 
     * @param orderRequest original order request (for potential retry logic)
     * @param runtimeException the exception that triggered the fallback
     * @return CompletableFuture with fallback error message
     */
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
