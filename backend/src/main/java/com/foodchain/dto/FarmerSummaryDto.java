package com.foodchain.dto;

public class FarmerSummaryDto {
    private Long farmerId;
    private String farmerName;
    private long numberOfProducts;
    private long totalStock;
    private long pendingOrders;
    private long deliveredOrders;

    public FarmerSummaryDto() {}

    public FarmerSummaryDto(Long farmerId, String farmerName, long numberOfProducts,
                            long totalStock, long pendingOrders, long deliveredOrders) {
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.numberOfProducts = numberOfProducts;
        this.totalStock = totalStock;
        this.pendingOrders = pendingOrders;
        this.deliveredOrders = deliveredOrders;
    }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public long getNumberOfProducts() { return numberOfProducts; }
    public void setNumberOfProducts(long numberOfProducts) { this.numberOfProducts = numberOfProducts; }

    public long getTotalStock() { return totalStock; }
    public void setTotalStock(long totalStock) { this.totalStock = totalStock; }

    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }

    public long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
}
