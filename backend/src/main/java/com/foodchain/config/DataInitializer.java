package com.foodchain.config;

import com.foodchain.entity.*;
import com.foodchain.repository.CustomerRepository;
import com.foodchain.repository.FarmerRepository;
import com.foodchain.repository.OrderRepository;
import com.foodchain.repository.ProductRepository;
import com.foodchain.repository.TrackingHistoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FarmerRepository farmerRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final TrackingHistoryRepository trackingHistoryRepository;

    public DataInitializer(FarmerRepository farmerRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           TrackingHistoryRepository trackingHistoryRepository) {
        this.farmerRepository = farmerRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.trackingHistoryRepository = trackingHistoryRepository;
    }

    @Override
    public void run(String... args) {
        if (farmerRepository.count() == 0) {
            seedInitialData();
        }
    }

    private void seedInitialData() {
        // Create Farmers
        Farmer lakshmi = farmerRepository.save(new Farmer("FARM-101", "Lakshmi Devi", "Vijayawada"));
        Farmer ravi = farmerRepository.save(new Farmer("FARM-102", "Ravi Kumar", "Gudivada"));
        Farmer meena = farmerRepository.save(new Farmer("FARM-103", "Meena Rao", "Machilipatnam"));
        Farmer suresh = farmerRepository.save(new Farmer("FARM-104", "Suresh Babu", "Vijayawada"));
        Farmer anitha = farmerRepository.save(new Farmer("FARM-105", "Anitha Prasad", "Pedana"));
        Farmer venkatesh = farmerRepository.save(new Farmer("FARM-106", "Venkatesh Rao", "Nuzvid"));
        Farmer priya = farmerRepository.save(new Farmer("FARM-107", "Priya Nair", "Gudivada"));
        Farmer mohan = farmerRepository.save(new Farmer("FARM-108", "Mohan Reddy", "Nuzvid"));

        // Create Seed Products
        Product p1 = productRepository.save(new Product(
                356344, "Organic Tomatoes", "Vine-ripened, locally grown tomatoes harvested this morning.",
                160, "kg", 42.0, lakshmi, lakshmi.getName(), "Vijayawada Warehouse", "Krishna", "Vegetables",
                "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?auto=format&fit=crop&w=900&q=85"
        ));

        Product p2 = productRepository.save(new Product(
                356345, "Fresh Potatoes", "Firm, clean potatoes from small family farms.",
                420, "kg", 28.0, ravi, ravi.getName(), "Gudivada Warehouse", "Krishna", "Vegetables",
                "https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=900&q=85"
        ));

        Product p3 = productRepository.save(new Product(
                356346, "Green Chilies", "Bright, fresh green chilies with a lively heat.",
                85, "kg", 64.0, meena, meena.getName(), "Machilipatnam Warehouse", "Krishna", "Vegetables",
                "https://images.unsplash.com/photo-1588252303782-cb80119abd6d?auto=format&fit=crop&w=900&q=85"
        ));

        Product p4 = productRepository.save(new Product(
                356347, "Organic Carrots", "Sweet, crisp carrots grown without synthetic pesticides.",
                230, "kg", 38.0, suresh, suresh.getName(), "Vijayawada Warehouse", "Krishna", "Vegetables",
                "https://images.unsplash.com/photo-1445282768818-728615cc910a?auto=format&fit=crop&w=900&q=85"
        ));

        Product p5 = productRepository.save(new Product(
                356348, "Fresh Onions", "A kitchen staple, sorted and ready for distribution.",
                610, "kg", 32.0, anitha, anitha.getName(), "Pedana Warehouse", "Krishna", "Vegetables",
                "https://images.unsplash.com/photo-1508747703725-719777637510?auto=format&fit=crop&w=900&q=85"
        ));

        Product p6 = productRepository.save(new Product(
                356349, "Rice", "Light, aromatic Sona Masoori rice sourced directly from Krishna farms.",
                960, "kg", 58.0, venkatesh, venkatesh.getName(), "Nuzvid Warehouse", "Krishna", "Grains",
                "https://images.unsplash.com/photo-1586201375761-83865001e31c?auto=format&fit=crop&w=900&q=85"
        ));

        Product p7 = productRepository.save(new Product(
                356350, "Bananas", "Naturally ripened bananas packed for a short journey.",
                145, "kg", 46.0, priya, priya.getName(), "Gudivada Warehouse", "Krishna", "Fruit",
                "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?auto=format&fit=crop&w=900&q=85"
        ));

        Product p8 = productRepository.save(new Product(
                356351, "Mangoes", "Seasonal Banganapalli mangoes from orchards around Nuzvid.",
                75, "kg", 120.0, mohan, mohan.getName(), "Nuzvid Warehouse", "Krishna", "Fruit",
                "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=900&q=85"
        ));

        // No predeclared customer credentials or fake orders.
        // All farmers and customers register dynamically through /api/auth and are stored in PostgreSQL (pgAdmin).
    }
}
