package com.foodchain.service;

import com.foodchain.dto.TrackingResponse;
import com.foodchain.dto.TrackingStepDto;
import com.foodchain.entity.Order;
import com.foodchain.entity.OrderStatus;
import com.foodchain.entity.TrackingHistory;
import com.foodchain.exception.ResourceNotFoundException;
import com.foodchain.repository.OrderRepository;
import com.foodchain.repository.TrackingHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TrackingService {

    private final OrderRepository orderRepository;
    private final TrackingHistoryRepository trackingHistoryRepository;
    private final OrderService orderService;

    private static final String[] ORDER_STAGES = {
            "Order Placed",
            "Prepared",
            "Dispatched",
            "In Transit",
            "Out for Delivery",
            "Delivered"
    };

    public TrackingService(OrderRepository orderRepository,
                           TrackingHistoryRepository trackingHistoryRepository,
                           OrderService orderService) {
        this.orderRepository = orderRepository;
        this.trackingHistoryRepository = trackingHistoryRepository;
        this.orderService = orderService;
    }

    public TrackingResponse getTrackingByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        List<TrackingHistory> historyList = trackingHistoryRepository.findByOrderIdOrderByCheckpointTimeAsc(orderId);

        TrackingResponse response = new TrackingResponse();
        response.setOrderId(order.getOrderId());
        response.setId(order.getOrderId());
        response.setProduct(order.getProductName());
        response.setProductCode(order.getProductCode());
        response.setQuantity(order.getQuantity());
        response.setFarmer(order.getFarmerName());
        response.setWarehouse(order.getWarehouse());
        response.setDestination(order.getDestinationCity());
        response.setStatus(order.getStatus().getDisplayName());

        if (order.getCreatedAt() != null) {
            response.setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        } else {
            response.setCreatedAt("Just now");
        }

        // Build 6-step timeline
        List<TrackingStepDto> timeline = new ArrayList<>();
        int currentStageIndex = getStageIndex(order.getStatus().getDisplayName());

        for (int i = 0; i < ORDER_STAGES.length; i++) {
            String stage = ORDER_STAGES[i];
            boolean isCompleted = i <= currentStageIndex;
            boolean isCurrent = i == currentStageIndex;

            String time = isCompleted ? "Completed" : "Pending";
            String description = getStageDescription(stage, order);

            // Find matching checkpoint if exists
            for (TrackingHistory h : historyList) {
                if (h.getStatus() != null && (h.getStatus().getDisplayName().equalsIgnoreCase(stage) || h.getStatus().name().equalsIgnoreCase(stage))) {
                    if (h.getCheckpointTime() != null) {
                        time = h.getCheckpointTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
                    }
                    if (h.getDescription() != null) {
                        description = h.getDescription();
                    }
                    break;
                }
            }

            timeline.add(new TrackingStepDto(stage, isCompleted, isCurrent, time, description));
        }

        response.setTimeline(timeline);
        return response;
    }

    public List<TrackingHistory> getTrackingHistory(String orderId) {
        if (!orderRepository.findByOrderId(orderId).isPresent()) {
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }
        return trackingHistoryRepository.findByOrderIdOrderByCheckpointTimeAsc(orderId);
    }

    public TrackingResponse updateTrackingStatus(String orderId, String statusName) {
        OrderStatus newStatus = OrderStatus.fromString(statusName);
        orderService.updateOrderStatus(orderId, newStatus);
        return getTrackingByOrderId(orderId);
    }

    private int getStageIndex(String displayName) {
        for (int i = 0; i < ORDER_STAGES.length; i++) {
            if (ORDER_STAGES[i].equalsIgnoreCase(displayName)) {
                return i;
            }
        }
        return 0;
    }

    private String getStageDescription(String stage, Order order) {
        return switch (stage) {
            case "Order Placed" -> "Order registered at " + order.getWarehouse();
            case "Prepared" -> "Produce sorted and packed for transit";
            case "Dispatched" -> "Departed warehouse towards destination hub";
            case "In Transit" -> "In transit along Krishna Zone route";
            case "Out for Delivery" -> "Courier dispatching to " + order.getDestinationCity();
            case "Delivered" -> "Delivered to customer at " + order.getDestinationCity();
            default -> "Status update";
        };
    }
}
