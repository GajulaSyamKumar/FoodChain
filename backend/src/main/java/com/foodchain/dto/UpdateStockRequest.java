package com.foodchain.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateStockRequest {

    @NotNull(message = "Quantity is required.")
    private Integer quantity;

    public UpdateStockRequest() {}

    public UpdateStockRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
