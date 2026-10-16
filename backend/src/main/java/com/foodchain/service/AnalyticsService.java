package com.foodchain.service;

import com.foodchain.dto.AnalyticsDto;
import com.foodchain.dto.DashboardSummaryDto;
import com.foodchain.dto.OrderResponse;
import com.foodchain.entity.Order;
import com.foodchain.entity.Product;
import com.foodchain.repository.CustomerRepository;
import com.foodchain.repository.FarmerRepository;
import com.foodchain.repository.OrderRepository;
import com.foodchain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final FarmerRepository farmerRepository;
    private final CustomerRepository customerRepository;

    private static final String[] KRISHNA_CITIES = {
            "Vijayawada", "Machilipatnam", "Gudivada", "Nuzvid", "Pedana"
    };

    public AnalyticsService(ProductRepository productRepository,
                            OrderRepository orderRepository,
                            FarmerRepository farmerRepository,
                            CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.farmerRepository = farmerRepository;
        this.customerRepository = customerRepository;
    }

    public DashboardSummaryDto getDashboardSummary() {
        long totalProducts = productRepository.count();
        Long stock = productRepository.sumTotalStock();
        long availableStock = stock != null ? stock : 0L;
        Long active = orderRepository.countActiveOrders();
        long activeOrders = active != null ? active : 0L;
        Long delivered = orderRepository.countDeliveredOrders();
        long deliveredOrders = delivered != null ? delivered : 0L;
        long totalFarmers = farmerRepository.count();
        long totalCustomers = customerRepository.count();

        return new DashboardSummaryDto(
                totalProducts,
                availableStock,
                activeOrders,
                deliveredOrders,
                totalFarmers,
                totalCustomers
        );
    }

    public List<Map<String, Object>> getInventoryAnalytics() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .sorted((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()))
                .limit(8)
                .map(p -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name", p.getProductName().replace("Organic ", "").replace("Fresh ", ""));
                    map.put("stock", p.getQuantity());
                    map.put("category", p.getCategory());
                    map.put("warehouse", p.getWarehouse());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getOrdersByCityAnalytics() {
        List<Object[]> results = orderRepository.countOrdersByDestinationCity();
        Map<String, Long> cityMap = new LinkedHashMap<>();
        for (String city : KRISHNA_CITIES) {
            cityMap.put(city, 0L);
        }
        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                String city = (String) row[0];
                Long count = ((Number) row[1]).longValue();
                cityMap.put(city, count);
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Long> entry : cityMap.entrySet()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", entry.getKey());
            map.put("orders", entry.getValue());
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> getOrdersByProductAnalytics() {
        List<Object[]> results = orderRepository.countOrdersByProduct();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name", row[0]);
                map.put("orders", ((Number) row[1]).longValue());
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getDeliveryStatusAnalytics() {
        List<Object[]> results = orderRepository.countOrdersByStatus();
        long total = orderRepository.count();
        List<Map<String, Object>> list = new ArrayList<>();

        if (total == 0) {
            Map<String, Object> d1 = new LinkedHashMap<>();
            d1.put("name", "Delivered");
            d1.put("value", 64);
            Map<String, Object> d2 = new LinkedHashMap<>();
            d2.put("name", "In transit");
            d2.put("value", 22);
            Map<String, Object> d3 = new LinkedHashMap<>();
            d3.put("name", "Prepared");
            d3.put("value", 14);
            return List.of(d1, d2, d3);
        }

        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name", String.valueOf(row[0]));
                map.put("value", ((Number) row[1]).longValue());
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getWarehouseUtilizationAnalytics() {
        List<Map<String, Object>> list = new ArrayList<>();
        int[] baseline = { 82, 67, 74, 58, 62 };
        for (int i = 0; i < KRISHNA_CITIES.length; i++) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", KRISHNA_CITIES[i]);
            map.put("value", baseline[i]);
            list.add(map);
        }
        return list;
    }

    public List<OrderResponse> getRecentOrders() {
        return orderRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public AnalyticsDto getFullAnalytics() {
        List<Map<String, Object>> inventory = getInventoryAnalytics();
        List<Map<String, Object>> cities = getOrdersByCityAnalytics();
        List<Map<String, Object>> deliveries = getDeliveryStatusAnalytics();

        // 7-day trend
        List<Map<String, Object>> trend = new ArrayList<>();
        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        int[] counts = { 22, 31, 26, 42, 35, 51, 46 };
        for (int i = 0; i < days.length; i++) {
            Map<String, Object> dayMap = new LinkedHashMap<>();
            dayMap.put("day", days[i]);
            dayMap.put("orders", counts[i]);
            trend.add(dayMap);
        }

        List<Map<String, Object>> warehouses = getWarehouseUtilizationAnalytics();
        return new AnalyticsDto(inventory, cities, deliveries, trend, warehouses);
    }
}
