package com.foodchain.service;

import com.foodchain.dto.CreateProductRequest;
import com.foodchain.dto.ProductDto;
import com.foodchain.dto.UpdateStockRequest;
import com.foodchain.entity.Farmer;
import com.foodchain.entity.Product;
import com.foodchain.exception.InvalidOperationException;
import com.foodchain.exception.ResourceNotFoundException;
import com.foodchain.repository.FarmerRepository;
import com.foodchain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final FarmerRepository farmerRepository;

    private static final java.util.Set<String> KRISHNA_CITIES = java.util.Set.of(
            "vijayawada", "machilipatnam", "gudivada", "nuzvid", "pedana"
    );

    public ProductService(ProductRepository productRepository, FarmerRepository farmerRepository) {
        this.productRepository = productRepository;
        this.farmerRepository = farmerRepository;
    }

    public List<ProductDto> getAllProducts(String city) {
        List<Product> products;
        if (city != null && !city.isBlank()) {
            products = productRepository.findByWarehouseContainingIgnoreCase(city.trim());
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    public ProductDto getProductById(Object id) {
        Product product = resolveProduct(id);
        return ProductDto.fromEntity(product);
    }

    public List<ProductDto> searchProductsByName(String name) {
        if (name == null || name.isBlank()) {
            return getAllProducts(null);
        }
        return productRepository.findByProductNameContainingIgnoreCase(name.trim())
                .stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public synchronized ProductDto addProduct(CreateProductRequest request) {
        String farmerName = request.getFarmer() != null && !request.getFarmer().isBlank()
                ? request.getFarmer().trim()
                : "Lakshmi Devi";
        String productName = request.getEffectiveProductName();

        if (productName.isBlank()) {
            throw new InvalidOperationException("Product name cannot be empty.");
        }
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidOperationException("Quantity must be greater than zero.");
        }

        if (request.getCity() != null && !request.getCity().isBlank()) {
            String cityNormalized = request.getCity().trim().toLowerCase();
            if (!KRISHNA_CITIES.contains(cityNormalized)) {
                throw new InvalidOperationException("Invalid city: " + request.getCity() + ". Supported Krishna Zone cities are: Vijayawada, Machilipatnam, Gudivada, Nuzvid, Pedana.");
            }
        }

        // Find or create farmer
        Farmer farmer = farmerRepository.findFirstByNameIgnoreCase(farmerName)
                .orElseGet(() -> {
                    Farmer newFarmer = new Farmer("FARM-" + System.currentTimeMillis(), farmerName, request.getCity());
                    return farmerRepository.save(newFarmer);
                });

        // Check if existing farmer has already listed the same product
        Optional<Product> existingOpt = productRepository.findByFarmerNameAndProductNameIgnoreCase(farmerName, productName);
        if (existingOpt.isPresent()) {
            Product existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                existing.setDescription(request.getDescription());
            }
            if (request.getPrice() != null && request.getPrice() > 0) {
                existing.setPrice(request.getPrice());
            }
            Product saved = productRepository.save(existing);
            return ProductDto.fromEntity(saved);
        }

        // Auto-generate product code (starts at 356344)
        Integer maxCode = productRepository.findMaxProductCode();
        int newProductCode = (maxCode == null || maxCode < 356344) ? 356344 : maxCode + 1;

        String city = request.getCity() != null && !request.getCity().isBlank() ? request.getCity().trim() : "Vijayawada";
        String warehouse = request.getWarehouse() != null && !request.getWarehouse().isBlank()
                ? request.getWarehouse().trim()
                : city + " Warehouse";

        String image = request.getImage() != null && !request.getImage().isBlank()
                ? request.getImage()
                : "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=900&q=85";

        Product product = new Product(
                newProductCode,
                productName,
                request.getDescription(),
                request.getQuantity(),
                request.getUnit() != null ? request.getUnit() : "kg",
                request.getPrice() != null ? request.getPrice() : 40.0,
                farmer,
                farmer.getName(),
                warehouse,
                "Krishna",
                request.getCategory() != null ? request.getCategory() : "Vegetables",
                image
        );

        Product saved = productRepository.save(product);
        return ProductDto.fromEntity(saved);
    }

    public ProductDto updateStock(Object id, UpdateStockRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new InvalidOperationException("Stock quantity must be non-negative.");
        }
        Product product = resolveProduct(id);
        product.setQuantity(product.getQuantity() + request.getQuantity());
        Product saved = productRepository.save(product);
        return ProductDto.fromEntity(saved);
    }

    public ProductDto updateProduct(Object id, CreateProductRequest request) {
        Product product = resolveProduct(id);
        if (request.getEffectiveProductName() != null && !request.getEffectiveProductName().isBlank()) {
            product.setProductName(request.getEffectiveProductName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null && request.getPrice() >= 0) {
            product.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null && request.getQuantity() >= 0) {
            product.setQuantity(request.getQuantity());
        }
        Product saved = productRepository.save(product);
        return ProductDto.fromEntity(saved);
    }

    public void deleteProduct(Object id) {
        Product product = resolveProduct(id);
        productRepository.delete(product);
    }

    public Product resolveProduct(Object id) {
        if (id == null) {
            throw new ResourceNotFoundException("Product identifier cannot be null.");
        }
        String idStr = String.valueOf(id).trim();
        try {
            int code = Integer.parseInt(idStr);
            // First try product code
            Optional<Product> byCode = productRepository.findByProductCode(code);
            if (byCode.isPresent()) return byCode.get();

            // Try DB primary key
            Optional<Product> byId = productRepository.findById((long) code);
            if (byId.isPresent()) return byId.get();
        } catch (NumberFormatException ignored) {}

        throw new ResourceNotFoundException("Product not found with identifier: " + id);
    }
}
