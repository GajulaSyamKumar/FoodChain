package com.foodchain.controller;

import com.foodchain.dto.AnalyticsDto;
import com.foodchain.dto.DashboardSummaryDto;
import com.foodchain.dto.OrderResponse;
import com.foodchain.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/api/dashboard/summary")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        return ResponseEntity.ok(analyticsService.getDashboardSummary());
    }

    @GetMapping("/api/analytics")
    public ResponseEntity<AnalyticsDto> getFullAnalytics() {
        return ResponseEntity.ok(analyticsService.getFullAnalytics());
    }

    @GetMapping("/api/analytics/inventory")
    public ResponseEntity<List<Map<String, Object>>> getInventoryAnalytics() {
        return ResponseEntity.ok(analyticsService.getInventoryAnalytics());
    }

    @GetMapping("/api/analytics/orders-by-city")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByCity() {
        return ResponseEntity.ok(analyticsService.getOrdersByCityAnalytics());
    }

    @GetMapping("/api/analytics/orders-by-product")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByProduct() {
        return ResponseEntity.ok(analyticsService.getOrdersByProductAnalytics());
    }

    @GetMapping("/api/analytics/delivery-status")
    public ResponseEntity<List<Map<String, Object>>> getDeliveryStatus() {
        return ResponseEntity.ok(analyticsService.getDeliveryStatusAnalytics());
    }

    @GetMapping("/api/analytics/warehouse-utilization")
    public ResponseEntity<List<Map<String, Object>>> getWarehouseUtilization() {
        return ResponseEntity.ok(analyticsService.getWarehouseUtilizationAnalytics());
    }

    @GetMapping("/api/analytics/recent-orders")
    public ResponseEntity<List<OrderResponse>> getRecentOrders() {
        return ResponseEntity.ok(analyticsService.getRecentOrders());
    }
}
