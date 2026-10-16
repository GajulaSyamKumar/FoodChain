package com.foodchain.service;

import com.foodchain.dto.OrderRequest;
import com.foodchain.dto.OrderResponse;
import com.foodchain.entity.*;
import com.foodchain.exception.InsufficientStockException;
import com.foodchain.exception.InvalidOperationException;
import com.foodchain.exception.ResourceNotFoundException;
import com.foodchain.repository.CustomerRepository;
import com.foodchain.repository.OrderRepository;
import com.foodchain.repository.ProductRepository;
import com.foodchain.repository.TrackingHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final TrackingHistoryRepository trackingHistoryRepository;

    private static final java.util.Set<String> KRISHNA_CITIES = java.util.Set.of(
            "vijayawada", "machilipatnam", "gudivada", "nuzvid", "pedana"
    );

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository,
                        TrackingHistoryRepository trackingHistoryRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.trackingHistoryRepository = trackingHistoryRepository;
    }

    public List<OrderResponse> getAllOrders(String status) {
        List<Order> orders;
        if (status != null && !status.isBlank()) {
            OrderStatus orderStatus = OrderStatus.fromString(status);
            orders = orderRepository.findByStatus(orderStatus);
        } else {
            orders = orderRepository.findAll();
        }
        return orders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return OrderResponse.fromEntity(order);
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        if (customerId == null) {
            return List.of();
        }
        return orderRepository.findByCustomerId(customerId).stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByFarmer(String farmerName) {
        if (farmerName == null || farmerName.isBlank()) {
            return List.of();
        }
        return orderRepository.findAll().stream()
                .filter(o -> o.getFarmerName() != null && o.getFarmerName().trim().equalsIgnoreCase(farmerName.trim()))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public synchronized OrderResponse createOrder(OrderRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidOperationException("Quantity must be at least 1.");
        }

        String destination = request.getEffectiveDestination();
        if (destination == null || !KRISHNA_CITIES.contains(destination.trim().toLowerCase())) {
            throw new InvalidOperationException("Invalid destination city: " + destination + ". Supported Krishna Zone cities are: Vijayawada, Machilipatnam, Gudivada, Nuzvid, Pedana.");
        }

        Product product = resolveProductWithLock(request.getProductId());

        if (product.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock available.");
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        }
        if (customer == null) {
            throw new InvalidOperationException("Customer account not identified. Please register or sign in as a customer before placing an order.");
        }

        // Deduct stock atomically
        product.setQuantity(product.getQuantity() - request.getQuantity());
        if (product.getQuantity() == 0) {
            product.setStatus("Out of Stock");
        }
        productRepository.save(product);

        // Generate Order ID (e.g., ORD1001, ORD1002, etc.)
        long nextNum = 1001 + orderRepository.count();
        String orderId = "ORD" + nextNum;
        while (orderRepository.findByOrderId(orderId).isPresent()) {
            nextNum++;
            orderId = "ORD" + nextNum;
        }

        double unitPrice = product.getPrice() != null ? product.getPrice() : 0.0;
        double totalPrice = unitPrice * request.getQuantity();

        Order order = new Order(
                orderId,
                customer,
                product,
                product.getProductName(),
                product.getProductCode(),
                product.getFarmerName(),
                request.getQuantity(),
                unitPrice,
                totalPrice,
                product.getWarehouse(),
                destination,
                OrderStatus.ORDER_PLACED
        );

        Order savedOrder = orderRepository.save(order);

        // Initialize tracking history
        TrackingHistory initialHistory = new TrackingHistory(
                savedOrder.getOrderId(),
                OrderStatus.ORDER_PLACED.getDisplayName(),
                "Order received and registered in Krishna food distribution network."
        );
        trackingHistoryRepository.save(initialHistory);

        return OrderResponse.fromEntity(savedOrder);
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidOperationException("Invalid status transition from " + order.getStatus().getDisplayName() + " to " + newStatus.getDisplayName());
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        TrackingHistory history = new TrackingHistory(
                orderId,
                newStatus.getDisplayName(),
                "Order status updated to " + newStatus.getDisplayName()
        );
        trackingHistoryRepository.save(history);

        return OrderResponse.fromEntity(saved);
    }

    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOperationException("Order cannot be cancelled after delivery.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Order is already cancelled.");
        }

        // Restore stock
        if (order.getProduct() != null) {
            Product product = order.getProduct();
            product.setQuantity(product.getQuantity() + order.getQuantity());
            if (product.getQuantity() > 0 && "Out of Stock".equalsIgnoreCase(product.getStatus())) {
                product.setStatus("Available");
            }
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        TrackingHistory history = new TrackingHistory(
                orderId,
                OrderStatus.CANCELLED.getDisplayName(),
                "Order was cancelled. Restocked " + order.getQuantity() + " kg back to inventory."
        );
        trackingHistoryRepository.save(history);

        return OrderResponse.fromEntity(saved);
    }

    private Product resolveProductWithLock(Object identifier) {
        if (identifier == null) {
            throw new InvalidOperationException("Product identifier cannot be null.");
        }
        String idStr = String.valueOf(identifier).trim();
        try {
            Long longId = Long.parseLong(idStr);
            return productRepository.findByIdWithPessimisticLock(longId)
                    .or(() -> productRepository.findByProductCodeWithPessimisticLock(longId.intValue()))
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID/Code: " + idStr));
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Invalid product identifier: " + idStr);
        }
    }
}
