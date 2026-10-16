package com.foodchain.repository;

import com.foodchain.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findFirstByNameIgnoreCase(String name);
    Optional<Farmer> findByNameIgnoreCase(String name);
    Optional<Farmer> findByFarmerCode(String farmerCode);
    Optional<Farmer> findByEmailIgnoreCase(String email);
}
