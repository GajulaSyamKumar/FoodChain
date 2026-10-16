package com.foodchain.dto;

import java.util.List;

public class TrackingResponse {
    private String orderId;
    private String id;
    private String product;
    private Integer productCode;
    private Integer quantity;
    private String farmer;
    private String warehouse;
    private String destination;
    private String status;
    private String createdAt;
    private List<TrackingStepDto> timeline;

    public TrackingResponse() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public Integer getProductCode() { return productCode; }
    public void setProductCode(Integer productCode) { this.productCode = productCode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

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

    public List<TrackingStepDto> getTimeline() { return timeline; }
    public void setTimeline(List<TrackingStepDto> timeline) { this.timeline = timeline; }
}
