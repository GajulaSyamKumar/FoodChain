package com.foodchain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", unique = true, nullable = false)
    private Integer productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 20)
    private String unit = "kg";

    private Double price = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Column(name = "farmer_name")
    private String farmerName;

    @Column(nullable = false)
    private String warehouse;

    private String zone = "Krishna";

    private String category = "Vegetables";

    @Column(length = 500)
    private String image;

    private String status = "Available";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Product() {}

    public Product(Integer productCode, String productName, String description, Integer quantity,
                   String unit, Double price, Farmer farmer, String farmerName, String warehouse,
                   String zone, String category, String image) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit != null ? unit : "kg";
        this.price = price != null ? price : 0.0;
        this.farmer = farmer;
        this.farmerName = farmerName;
        this.warehouse = warehouse;
        this.zone = zone != null ? zone : "Krishna";
        this.category = category != null ? category : "Vegetables";
        this.image = image;
        this.status = "Available";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
