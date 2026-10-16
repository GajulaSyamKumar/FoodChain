package com.foodchain;

import com.foodchain.dto.AnalyticsDto;
import com.foodchain.dto.DashboardSummaryDto;
import com.foodchain.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class AnalyticsServiceTests {

    @Autowired
    private AnalyticsService analyticsService;

    @Test
    void testDashboardSummaryNotNull() {
        DashboardSummaryDto summary = analyticsService.getDashboardSummary();
        assertNotNull(summary);
        assertTrue(summary.getTotalProducts() >= 0);
        assertTrue(summary.getAvailableStock() >= 0);
        assertTrue(summary.getActiveOrders() >= 0);
        assertTrue(summary.getDeliveredOrders() >= 0);
    }

    @Test
    void testInventoryAnalytics() {
        List<Map<String, Object>> inv = analyticsService.getInventoryAnalytics();
        assertNotNull(inv);
    }

    @Test
    void testOrdersByCityAnalytics() {
        List<Map<String, Object>> cities = analyticsService.getOrdersByCityAnalytics();
        assertNotNull(cities);
        assertEquals(5, cities.size()); // All 5 Krishna Zone cities present
    }

    @Test
    void testDeliveryStatusAnalytics() {
        List<Map<String, Object>> deliveries = analyticsService.getDeliveryStatusAnalytics();
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());
    }

    @Test
    void testFullAnalytics() {
        AnalyticsDto full = analyticsService.getFullAnalytics();
        assertNotNull(full);
        assertNotNull(full.getCities());
        assertNotNull(full.getDeliveries());
        assertNotNull(full.getTrend());
        assertNotNull(full.getWarehouses());
    }
}
