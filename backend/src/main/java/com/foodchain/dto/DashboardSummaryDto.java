package com.foodchain.dto;

public class DashboardSummaryDto {
    private long totalProducts;
    private long availableStock;
    private long activeOrders;
    private long deliveredOrders;
    private long totalFarmers;
    private long totalCustomers;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(long totalProducts, long availableStock, long activeOrders,
                               long deliveredOrders, long totalFarmers, long totalCustomers) {
        this.totalProducts = totalProducts;
        this.availableStock = availableStock;
        this.activeOrders = activeOrders;
        this.deliveredOrders = deliveredOrders;
        this.totalFarmers = totalFarmers;
        this.totalCustomers = totalCustomers;
    }

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

    public long getAvailableStock() { return availableStock; }
    public void setAvailableStock(long availableStock) { this.availableStock = availableStock; }

    public long getActiveOrders() { return activeOrders; }
    public void setActiveOrders(long activeOrders) { this.activeOrders = activeOrders; }

    public long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }

    public long getTotalFarmers() { return totalFarmers; }
    public void setTotalFarmers(long totalFarmers) { this.totalFarmers = totalFarmers; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
}
