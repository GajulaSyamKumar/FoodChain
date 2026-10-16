package com.foodchain.dto;

import com.foodchain.entity.Product;

public class ProductDto {
    private String id;
    private Integer productCode;
    private String product;
    private String productName;
    private String description;
    private Integer quantity;
    private String unit;
    private Double price;
    private String farmer;
    private Long farmerId;
    private String warehouse;
    private String zone;
    private String category;
    private String image;
    private String status;

    public ProductDto() {}

    public static ProductDto fromEntity(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(String.valueOf(entity.getProductCode()));
        dto.setProductCode(entity.getProductCode());
        dto.setProduct(entity.getProductName());
        dto.setProductName(entity.getProductName());
        dto.setDescription(entity.getDescription());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setPrice(entity.getPrice());
        dto.setFarmer(entity.getFarmerName());
        dto.setFarmerId(entity.getFarmer() != null ? entity.getFarmer().getId() : null);
        dto.setWarehouse(entity.getWarehouse());
        dto.setZone(entity.getZone());
        dto.setCategory(entity.getCategory());
        dto.setImage(entity.getImage());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getProductCode() { return productCode; }
    public void setProductCode(Integer productCode) { this.productCode = productCode; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getFarmer() { return farmer; }
    public void setFarmer(String farmer) { this.farmer = farmer; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getWarehouse() { return warehouse; }
    public void setWarehouse(String warehouse) { this.warehouse = warehouse; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
