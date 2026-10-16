package com.foodchain.service;

import com.foodchain.dto.CustomerDto;
import com.foodchain.dto.CustomerSummaryDto;
import com.foodchain.dto.OrderResponse;
import com.foodchain.entity.Customer;
import com.foodchain.entity.Order;
import com.foodchain.exception.ResourceNotFoundException;
import com.foodchain.repository.CustomerRepository;
import com.foodchain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CustomerDto createCustomer(CustomerDto request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setCustomerCode(request.getCustomerCode() != null ? request.getCustomerCode() : "CUST-" + System.currentTimeMillis());
        Customer saved = customerRepository.save(customer);
        return CustomerDto.fromEntity(saved);
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return CustomerDto.fromEntity(customer);
    }

    public List<OrderResponse> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CustomerSummaryDto getCustomerSummary(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        long total = orders.size();
        long active = orders.stream().filter(o -> !o.getStatus().name().equals("DELIVERED") && !o.getStatus().name().equals("CANCELLED")).count();
        long completed = orders.stream().filter(o -> o.getStatus().name().equals("DELIVERED")).count();

        return new CustomerSummaryDto(customer.getId(), customer.getName(), total, active, completed);
    }
}
