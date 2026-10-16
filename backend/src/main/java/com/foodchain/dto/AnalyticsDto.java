package com.foodchain.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsDto {
    private List<Map<String, Object>> inventory;
    private List<Map<String, Object>> cities;
    private List<Map<String, Object>> deliveries;
    private List<Map<String, Object>> trend;
    private List<Map<String, Object>> warehouses;

    public AnalyticsDto() {}

    public AnalyticsDto(List<Map<String, Object>> inventory, List<Map<String, Object>> cities,
                        List<Map<String, Object>> deliveries, List<Map<String, Object>> trend,
                        List<Map<String, Object>> warehouses) {
        this.inventory = inventory;
        this.cities = cities;
        this.deliveries = deliveries;
        this.trend = trend;
        this.warehouses = warehouses;
    }

    public List<Map<String, Object>> getInventory() { return inventory; }
    public void setInventory(List<Map<String, Object>> inventory) { this.inventory = inventory; }

    public List<Map<String, Object>> getCities() { return cities; }
    public void setCities(List<Map<String, Object>> cities) { this.cities = cities; }

    public List<Map<String, Object>> getDeliveries() { return deliveries; }
    public void setDeliveries(List<Map<String, Object>> deliveries) { this.deliveries = deliveries; }

    public List<Map<String, Object>> getTrend() { return trend; }
    public void setTrend(List<Map<String, Object>> trend) { this.trend = trend; }

    public List<Map<String, Object>> getWarehouses() { return warehouses; }
    public void setWarehouses(List<Map<String, Object>> warehouses) { this.warehouses = warehouses; }
}
