package com.programmingtechie.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Product response")
public class ProductResponse {
    @Schema(description = "Product ID", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Product name", example = "iPhone 13")
    private String name;
    
    @Schema(description = "Product description", example = "Apple iPhone 13 with 128GB storage")
    private String description;
    
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    public ProductResponse() {}

    public ProductResponse(String id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static ProductResponseBuilder builder() {
        return new ProductResponseBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public static class ProductResponseBuilder {
        private String id;
        private String name;
        private String description;
        private BigDecimal price;

        public ProductResponseBuilder id(String id) { this.id = id; return this; }
        public ProductResponseBuilder name(String name) { this.name = name; return this; }
        public ProductResponseBuilder description(String description) { this.description = description; return this; }
        public ProductResponseBuilder price(BigDecimal price) { this.price = price; return this; }
        public ProductResponse build() { return new ProductResponse(id, name, description, price); }
    }
}
