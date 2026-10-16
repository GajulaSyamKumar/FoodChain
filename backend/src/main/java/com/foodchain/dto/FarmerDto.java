package com.foodchain.dto;

import com.foodchain.entity.Farmer;

public class FarmerDto {
    private Long id;
    private String farmerCode;
    private String name;
    private String city;

    public FarmerDto() {}

    public static FarmerDto fromEntity(Farmer farmer) {
        FarmerDto dto = new FarmerDto();
        dto.setId(farmer.getId());
        dto.setFarmerCode(farmer.getFarmerCode());
        dto.setName(farmer.getName());
        dto.setCity(farmer.getCity());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFarmerCode() { return farmerCode; }
    public void setFarmerCode(String farmerCode) { this.farmerCode = farmerCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
