package com.foodchain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", unique = true)
    private String customerCode;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phone;

    private String city = "Vijayawada";

    private String address;

    private String password = "1234";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Customer() {}

    public Customer(String customerCode, String name, String email) {
        this.customerCode = customerCode;
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public Customer(String customerCode, String name, String email, String phone, String city, String address, String password) {
        this.customerCode = customerCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = (city != null && !city.isBlank()) ? city : "Vijayawada";
        this.address = address;
        this.password = (password != null && !password.isBlank()) ? password : "1234";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
