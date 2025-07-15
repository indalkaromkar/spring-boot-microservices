package com.programmingtechie.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Product creation request")
public class ProductRequest {
    @Schema(description = "Product name", example = "iPhone 13")
    private String name;
    
    @Schema(description = "Product description", example = "Apple iPhone 13 with 128GB storage")
    private String description;
    
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    public ProductRequest() {}

    public ProductRequest(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
