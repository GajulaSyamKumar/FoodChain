package com.foodchain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_code", nullable = false)
    private Integer productCode;

    @Column(name = "farmer_name")
    private String farmerName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_at_order", nullable = false)
    private Double unitPriceAtOrder;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String warehouse;

    @Column(name = "destination_city", nullable = false)
    private String destinationCity;

    private String zone = "Krishna";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.ORDER_PLACED;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Order() {}

    public Order(String orderId, Customer customer, Product product, String productName,
                 Integer productCode, String farmerName, Integer quantity, Double unitPriceAtOrder,
                 Double totalPrice, String warehouse, String destinationCity, OrderStatus status) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
        this.productName = productName;
        this.productCode = productCode;
        this.farmerName = farmerName;
        this.quantity = quantity;
        this.unitPriceAtOrder = unitPriceAtOrder;
        this.totalPrice = totalPrice;
        this.warehouse = warehouse;
        this.destinationCity = destinationCity;
        this.zone = "Krishna";
        this.status = status != null ? status : OrderStatus.ORDER_PLACED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPriceAtOrder() {
        return unitPriceAtOrder;
    }

    public void setUnitPriceAtOrder(Double unitPriceAtOrder) {
        this.unitPriceAtOrder = unitPriceAtOrder;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
