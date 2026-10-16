package com.foodchain.repository;

import com.foodchain.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(Integer productCode);

    @Query("SELECT p FROM Product p WHERE LOWER(p.farmerName) = LOWER(:farmerName) AND LOWER(p.productName) = LOWER(:productName)")
    Optional<Product> findByFarmerNameAndProductNameIgnoreCase(@Param("farmerName") String farmerName,
                                                              @Param("productName") String productName);

    List<Product> findByProductNameContainingIgnoreCase(String name);

    List<Product> findByWarehouseContainingIgnoreCase(String city);

    List<Product> findByFarmerId(Long farmerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productCode = :productCode")
    Optional<Product> findByProductCodeWithPessimisticLock(@Param("productCode") Integer productCode);

    @Query("SELECT COALESCE(MAX(p.productCode), 356343) FROM Product p")
    Integer findMaxProductCode();

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p")
    Long sumTotalStock();

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p WHERE p.farmer.id = :farmerId")
    Long sumStockByFarmerId(@Param("farmerId") Long farmerId);
}
