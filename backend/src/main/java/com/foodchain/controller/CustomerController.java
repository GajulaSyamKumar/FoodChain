package com.foodchain.controller;

import com.foodchain.dto.CustomerDto;
import com.foodchain.dto.CustomerSummaryDto;
import com.foodchain.dto.OrderResponse;
import com.foodchain.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerOrders(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<CustomerSummaryDto> getCustomerSummary(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerSummary(id));
    }
}
