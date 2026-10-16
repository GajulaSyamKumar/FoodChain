package com.foodchain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    private Long customerId;

    @NotNull(message = "Product identifier is required.")
    private Object productId; // Accepts either String ID (productCode) or Long ID

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

    @NotBlank(message = "Destination city is required.")
    private String destination;

    private String destinationCity;

    public OrderRequest() {}

    public String getEffectiveDestination() {
        if (destination != null && !destination.isBlank()) return destination.trim();
        if (destinationCity != null && !destinationCity.isBlank()) return destinationCity.trim();
        return "Vijayawada";
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Object getProductId() { return productId; }
    public void setProductId(Object productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
}
