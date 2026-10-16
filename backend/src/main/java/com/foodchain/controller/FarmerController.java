package com.foodchain.controller;

import com.foodchain.dto.FarmerDto;
import com.foodchain.dto.FarmerSummaryDto;
import com.foodchain.dto.ProductDto;
import com.foodchain.service.FarmerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public ResponseEntity<List<FarmerDto>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerDto> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDto>> getFarmerProducts(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerProducts(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<com.foodchain.dto.OrderResponse>> getFarmerOrders(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerOrders(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<FarmerSummaryDto> getFarmerSummary(@PathVariable Long id) {
        return ResponseEntity.ok(farmerService.getFarmerSummary(id));
    }

    @PostMapping
    public ResponseEntity<FarmerDto> createFarmer(@RequestBody FarmerDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(farmerService.createFarmer(request));
    }
}
