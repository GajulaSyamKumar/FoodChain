package com.foodchain.repository;

import com.foodchain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerCode(String customerCode);
    Optional<Customer> findByEmailIgnoreCase(String email);
    Optional<Customer> findByNameIgnoreCase(String name);
    Optional<Customer> findFirstByNameIgnoreCase(String name);
}
