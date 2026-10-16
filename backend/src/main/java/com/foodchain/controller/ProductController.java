package com.foodchain.controller;

import com.foodchain.dto.CreateProductRequest;
import com.foodchain.dto.ProductDto;
import com.foodchain.dto.UpdateStockRequest;
import com.foodchain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) String city) {
        return ResponseEntity.ok(productService.getAllProducts(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Object id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductDto created = productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Object id, @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDto> updateStock(@PathVariable Object id, @Valid @RequestBody UpdateStockRequest request) {
        return ResponseEntity.ok(productService.updateStock(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
