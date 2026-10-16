package com.foodchain.dto;

public class CustomerSummaryDto {
    private Long customerId;
    private String customerName;
    private long totalOrders;
    private long activeOrders;
    private long completedOrders;

    public CustomerSummaryDto() {}

    public CustomerSummaryDto(Long customerId, String customerName, long totalOrders,
                              long activeOrders, long completedOrders) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalOrders = totalOrders;
        this.activeOrders = activeOrders;
        this.completedOrders = completedOrders;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public long getActiveOrders() { return activeOrders; }
    public void setActiveOrders(long activeOrders) { this.activeOrders = activeOrders; }

    public long getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(long completedOrders) { this.completedOrders = completedOrders; }
}
