package com.foodchain.controller;

import com.foodchain.dto.CustomerDto;
import com.foodchain.dto.FarmerDto;
import com.foodchain.entity.Customer;
import com.foodchain.entity.Farmer;
import com.foodchain.repository.CustomerRepository;
import com.foodchain.repository.FarmerRepository;
import com.foodchain.service.CustomerService;
import com.foodchain.service.FarmerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final FarmerRepository farmerRepository;
    private final CustomerRepository customerRepository;
    private final FarmerService farmerService;
    private final CustomerService customerService;

    public AuthController(FarmerRepository farmerRepository,
                          CustomerRepository customerRepository,
                          FarmerService farmerService,
                          CustomerService customerService) {
        this.farmerRepository = farmerRepository;
        this.customerRepository = customerRepository;
        this.farmerService = farmerService;
        this.customerService = customerService;
    }

    // ==========================================
    // FARMER (USER / PRODUCER) AUTHENTICATION
    // ==========================================

    @PostMapping("/farmer/login")
    public ResponseEntity<?> loginFarmer(@RequestBody Map<String, String> credentials) {
        String identifier = credentials.get("identifier");
        String password = credentials.get("password");

        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Both username/code and password are required."));
        }

        String trimmed = identifier.trim();

        // Search by farmer code, email, or name
        Optional<Farmer> farmerOpt = farmerRepository.findByFarmerCode(trimmed);
        if (farmerOpt.isEmpty()) {
            farmerOpt = farmerRepository.findByEmailIgnoreCase(trimmed);
        }
        if (farmerOpt.isEmpty()) {
            farmerOpt = farmerRepository.findByNameIgnoreCase(trimmed);
        }
        if (farmerOpt.isEmpty()) {
            farmerOpt = farmerRepository.findFirstByNameIgnoreCase(trimmed);
        }

        if (farmerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Farmer account '" + identifier + "' not found in database. Please register first."));
        }

        Farmer farmer = farmerOpt.get();
        // Verify password stored in PostgreSQL
        String storedPassword = farmer.getPassword() != null ? farmer.getPassword() : "1234";
        if (!storedPassword.equals(password.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect password for farmer account."));
        }

        return ResponseEntity.ok(FarmerDto.fromEntity(farmer));
    }

    @PostMapping("/farmer/register")
    public ResponseEntity<?> registerFarmer(@RequestBody FarmerDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Farmer name is required."));
        }

        // Check if name or code already exists
        if (farmerRepository.findByNameIgnoreCase(request.getName().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "A farmer with name '" + request.getName() + "' already exists. Please choose a distinct name or sign in."));
        }

        FarmerDto created = farmerService.createFarmer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ==========================================
    // CUSTOMER (BUYER / CONSUMER) AUTHENTICATION
    // ==========================================

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> credentials) {
        String identifier = credentials.get("identifier");
        String password = credentials.get("password");

        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Both email/customer code and password are required."));
        }

        String trimmed = identifier.trim();

        // Search by customer code, email, or name
        Optional<Customer> customerOpt = customerRepository.findByCustomerCode(trimmed);
        if (customerOpt.isEmpty()) {
            customerOpt = customerRepository.findByEmailIgnoreCase(trimmed);
        }
        if (customerOpt.isEmpty()) {
            customerOpt = customerRepository.findByNameIgnoreCase(trimmed);
        }
        if (customerOpt.isEmpty()) {
            customerOpt = customerRepository.findFirstByNameIgnoreCase(trimmed);
        }

        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Customer account '" + identifier + "' not found in database. Please register first."));
        }

        Customer customer = customerOpt.get();
        // Verify password stored in PostgreSQL
        String storedPassword = customer.getPassword() != null ? customer.getPassword() : "1234";
        if (!storedPassword.equals(password.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect password for customer account."));
        }

        return ResponseEntity.ok(CustomerDto.fromEntity(customer));
    }

    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Customer name is required."));
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (customerRepository.findByEmailIgnoreCase(request.getEmail().trim()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "A customer with email '" + request.getEmail() + "' already exists."));
            }
        }

        CustomerDto created = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
