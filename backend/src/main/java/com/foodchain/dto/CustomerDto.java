package com.foodchain.dto;

import com.foodchain.entity.Customer;

public class CustomerDto {
    private Long id;
    private String customerCode;
    private String name;
    private String email;

    public CustomerDto() {}

    public static CustomerDto fromEntity(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
