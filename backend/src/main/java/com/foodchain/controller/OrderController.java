package com.foodchain.controller;

import com.foodchain.dto.OrderRequest;
import com.foodchain.dto.OrderResponse;
import com.foodchain.entity.OrderStatus;
import com.foodchain.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/farmer/{farmerName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByFarmer(@PathVariable String farmerName) {
        return ResponseEntity.ok(orderService.getOrdersByFarmer(farmerName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId,
                                                          @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        OrderStatus status = OrderStatus.fromString(statusStr);
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
