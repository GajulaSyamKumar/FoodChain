package com.foodchain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_history")
public class TrackingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private Order order;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String description;

    @Column(name = "checkpoint_time")
    private LocalDateTime checkpointTime = LocalDateTime.now();

    public TrackingHistory() {}

    public TrackingHistory(Order order, String orderId, OrderStatus status, String description) {
        this.order = order;
        this.orderId = orderId;
        this.status = status;
        this.description = description;
        this.checkpointTime = LocalDateTime.now();
    }

    public TrackingHistory(String orderId, OrderStatus status, String description) {
        this.orderId = orderId;
        this.status = status;
        this.description = description;
        this.checkpointTime = LocalDateTime.now();
    }

    public TrackingHistory(String orderId, String statusName, String description) {
        this.orderId = orderId;
        this.status = OrderStatus.fromString(statusName);
        this.description = description;
        this.checkpointTime = LocalDateTime.now();
    }

    public TrackingHistory(Order order, OrderStatus status, String description) {
        this.order = order;
        this.orderId = order != null ? order.getOrderId() : null;
        this.status = status;
        this.description = description;
        this.checkpointTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCheckpointTime() {
        return checkpointTime;
    }

    public void setCheckpointTime(LocalDateTime checkpointTime) {
        this.checkpointTime = checkpointTime;
    }
}
