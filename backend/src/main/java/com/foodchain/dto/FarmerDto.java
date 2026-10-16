package com.foodchain.dto;

import com.foodchain.entity.Farmer;

public class FarmerDto {
    private Long id;
    private String farmerCode;
    private String name;
    private String city;
    private String email;
    private String phone;
    private String password;

    public FarmerDto() {}

    public static FarmerDto fromEntity(Farmer farmer) {
        FarmerDto dto = new FarmerDto();
        dto.setId(farmer.getId());
        dto.setFarmerCode(farmer.getFarmerCode());
        dto.setName(farmer.getName());
        dto.setCity(farmer.getCity());
        dto.setEmail(farmer.getEmail());
        dto.setPhone(farmer.getPhone());
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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
