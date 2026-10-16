package com.foodchain.entity;

public enum OrderStatus {
    ORDER_PLACED("Order Placed"),
    PREPARED("Prepared"),
    DISPATCHED("Dispatched"),
    IN_TRANSIT("In Transit"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canTransitionTo(OrderStatus next) {
        if (this == next) return true;
        if (this == DELIVERED || this == CANCELLED) return false;
        if (next == CANCELLED) return this != DELIVERED;
        return switch (this) {
            case ORDER_PLACED -> next == PREPARED;
            case PREPARED -> next == DISPATCHED;
            case DISPATCHED -> next == IN_TRANSIT;
            case IN_TRANSIT -> next == OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> next == DELIVERED;
            default -> false;
        };
    }

    public static OrderStatus fromString(String text) {
        if (text == null) return ORDER_PLACED;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(text) ||
                status.displayName.equalsIgnoreCase(text) ||
                status.name().replace("_", " ").equalsIgnoreCase(text)) {
                return status;
            }
        }
        return ORDER_PLACED;
    }
}
