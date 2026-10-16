package com.foodchain.controller;

import com.foodchain.dto.FarmerDto;
import com.foodchain.dto.FarmerSummaryDto;
import com.foodchain.dto.OrderResponse;
import com.foodchain.dto.ProductDto;
import com.foodchain.service.FarmerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController — Exposes endpoints for the User (Farmer / Producer) domain.
 * Differentiates from CustomerController (Buyer / Consumer domain).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final FarmerService farmerService;

    public UserController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public ResponseEntity<List<FarmerDto>> getAllUsers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDto>> getUserProducts(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerProducts(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getUserIncomingOrders(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerOrders(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<FarmerSummaryDto> getUserSummary(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerSummary(id));
    }

    @PostMapping
    public ResponseEntity<FarmerDto> createUser(@RequestBody FarmerDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerService.createFarmer(request));
    }
}
