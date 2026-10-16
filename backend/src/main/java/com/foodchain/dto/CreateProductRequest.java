package com.foodchain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {

    private String farmer;
    private Long farmerId;

    @NotBlank(message = "Product name is required.")
    private String product;

    private String productName;

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be greater than zero.")
    private Integer quantity;

    private String city = "Vijayawada";
    private String warehouse;
    private String description;
    private Double price;
    private String category = "Vegetables";
    private String unit = "kg";
    private String image;

    public CreateProductRequest() {}

    public String getEffectiveProductName() {
        if (product != null && !product.isBlank()) return product.trim();
        if (productName != null && !productName.isBlank()) return productName.trim();
        return "";
    }

    public String getFarmer() { return farmer; }
    public void setFarmer(String farmer) { this.farmer = farmer; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getWarehouse() { return warehouse; }
    public void setWarehouse(String warehouse) { this.warehouse = warehouse; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
