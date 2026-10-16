package com.foodchain.dto;

import com.foodchain.entity.Order;

import java.time.format.DateTimeFormatter;

public class OrderResponse {
    private String id;
    private String orderId;
    private String product;
    private String productName;
    private Integer productCode;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String farmer;
    private String warehouse;
    private String destination;
    private String status;
    private String createdAt;

    public OrderResponse() {}

    public static OrderResponse fromEntity(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getOrderId());
        dto.setOrderId(order.getOrderId());
        dto.setProduct(order.getProductName());
        dto.setProductName(order.getProductName());
        dto.setProductCode(order.getProductCode());
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPriceAtOrder());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setFarmer(order.getFarmerName());
        dto.setWarehouse(order.getWarehouse());
        dto.setDestination(order.getDestinationCity());
        dto.setStatus(order.getStatus().getDisplayName());
        if (order.getCreatedAt() != null) {
            dto.setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        } else {
            dto.setCreatedAt("Just now");
        }
        return dto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getProductCode() { return productCode; }
    public void setProductCode(Integer productCode) { this.productCode = productCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getFarmer() { return farmer; }
    public void setFarmer(String farmer) { this.farmer = farmer; }

    public String getWarehouse() { return warehouse; }
    public void setWarehouse(String warehouse) { this.warehouse = warehouse; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
