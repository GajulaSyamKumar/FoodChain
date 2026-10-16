package com.foodchain.repository;

import com.foodchain.entity.Order;
import com.foodchain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status NOT IN (com.foodchain.entity.OrderStatus.DELIVERED, com.foodchain.entity.OrderStatus.CANCELLED)")
    Long countActiveOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = com.foodchain.entity.OrderStatus.DELIVERED")
    Long countDeliveredOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.farmerName = :farmerName AND o.status NOT IN (com.foodchain.entity.OrderStatus.DELIVERED, com.foodchain.entity.OrderStatus.CANCELLED)")
    Long countActiveOrdersByFarmer(@Param("farmerName") String farmerName);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.farmerName = :farmerName AND o.status = com.foodchain.entity.OrderStatus.DELIVERED")
    Long countDeliveredOrdersByFarmer(@Param("farmerName") String farmerName);

    @Query("SELECT o.destinationCity, COUNT(o) FROM Order o GROUP BY o.destinationCity")
    List<Object[]> countOrdersByDestinationCity();

    @Query("SELECT o.productName, COUNT(o) FROM Order o GROUP BY o.productName")
    List<Object[]> countOrdersByProduct();

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
}
