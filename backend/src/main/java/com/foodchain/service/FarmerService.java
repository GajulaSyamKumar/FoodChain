package com.foodchain.service;

import com.foodchain.dto.FarmerDto;
import com.foodchain.dto.FarmerSummaryDto;
import com.foodchain.dto.ProductDto;
import com.foodchain.entity.Farmer;
import com.foodchain.exception.ResourceNotFoundException;
import com.foodchain.repository.FarmerRepository;
import com.foodchain.repository.OrderRepository;
import com.foodchain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public FarmerService(FarmerRepository farmerRepository,
                         ProductRepository productRepository,
                         OrderRepository orderRepository) {
        this.farmerRepository = farmerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<FarmerDto> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(FarmerDto::fromEntity)
                .collect(Collectors.toList());
    }

    public FarmerDto getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + id));
        return FarmerDto.fromEntity(farmer);
    }

    public FarmerDto createFarmer(FarmerDto request) {
        Farmer farmer = new Farmer();
        farmer.setName(request.getName());
        farmer.setCity(request.getCity() != null ? request.getCity() : "Vijayawada");
        farmer.setFarmerCode(request.getFarmerCode() != null ? request.getFarmerCode() : "FARM-" + System.currentTimeMillis());
        Farmer saved = farmerRepository.save(farmer);
        return FarmerDto.fromEntity(saved);
    }

    public List<ProductDto> getFarmerProducts(Long farmerId) {
        return productRepository.findByFarmerId(farmerId).stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<com.foodchain.dto.OrderResponse> getFarmerOrders(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));
        return orderRepository.findAll().stream()
                .filter(o -> o.getFarmerName() != null && o.getFarmerName().trim().equalsIgnoreCase(farmer.getName().trim()))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(com.foodchain.dto.OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public FarmerSummaryDto getFarmerSummary(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + farmerId));

        long count = productRepository.findByFarmerId(farmerId).size();
        long stock = productRepository.sumStockByFarmerId(farmerId);
        long pending = orderRepository.countActiveOrdersByFarmer(farmer.getName());
        long delivered = orderRepository.countDeliveredOrdersByFarmer(farmer.getName());

        return new FarmerSummaryDto(farmer.getId(), farmer.getName(), count, stock, pending, delivered);
    }
}
