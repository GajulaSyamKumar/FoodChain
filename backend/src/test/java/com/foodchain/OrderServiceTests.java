package com.foodchain;

import com.foodchain.dto.CreateProductRequest;
import com.foodchain.dto.OrderRequest;
import com.foodchain.dto.OrderResponse;
import com.foodchain.dto.ProductDto;
import com.foodchain.entity.OrderStatus;
import com.foodchain.exception.InsufficientStockException;
import com.foodchain.exception.InvalidOperationException;
import com.foodchain.service.OrderService;
import com.foodchain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Test
    void testSuccessfulOrderWhenStockIsSufficient() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Test Order");
        req.setProduct("Fresh Apples");
        req.setQuantity(50);
        req.setCity("Vijayawada");
        req.setPrice(100.0);
        ProductDto product = productService.addProduct(req);

        OrderRequest orderReq = new OrderRequest();
        orderReq.setProductId(product.getProductCode());
        orderReq.setQuantity(10);
        orderReq.setDestination("Gudivada");

        OrderResponse order = orderService.createOrder(orderReq);
        assertNotNull(order);
        assertTrue(order.getOrderId().startsWith("ORD"));
        assertEquals(10, order.getQuantity());
        assertEquals(1000.0, order.getTotalPrice());

        // Verify stock deducted
        ProductDto updated = productService.getProductById(product.getProductCode());
        assertEquals(40, updated.getQuantity());
    }

    @Test
    void testInsufficientStockOrderRejected() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Low Stock");
        req.setProduct("Fresh Strawberries");
        req.setQuantity(5);
        req.setCity("Machilipatnam");
        ProductDto product = productService.addProduct(req);

        OrderRequest orderReq = new OrderRequest();
        orderReq.setProductId(product.getProductCode());
        orderReq.setQuantity(20);
        orderReq.setDestination("Vijayawada");

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderReq));
    }

    @Test
    void testInvalidDestinationCityRejected() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer City Order");
        req.setProduct("Watermelon");
        req.setQuantity(30);
        req.setCity("Gudivada");
        ProductDto product = productService.addProduct(req);

        OrderRequest orderReq = new OrderRequest();
        orderReq.setProductId(product.getProductCode());
        orderReq.setQuantity(5);
        orderReq.setDestination("Delhi"); // Invalid city

        assertThrows(InvalidOperationException.class, () -> orderService.createOrder(orderReq));
    }

    @Test
    void testTrackingStatusTransitions() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Track");
        req.setProduct("Guavas");
        req.setQuantity(40);
        req.setCity("Vijayawada");
        ProductDto product = productService.addProduct(req);

        OrderRequest orderReq = new OrderRequest();
        orderReq.setProductId(product.getProductCode());
        orderReq.setQuantity(5);
        orderReq.setDestination("Machilipatnam");
        OrderResponse order = orderService.createOrder(orderReq);

        // Step 1: ORDER_PLACED -> PREPARED
        OrderResponse s1 = orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PREPARED);
        assertEquals(OrderStatus.PREPARED.getDisplayName(), s1.getStatus());

        // Invalid skip: PREPARED -> DELIVERED should throw InvalidOperationException
        assertThrows(InvalidOperationException.class, () ->
                orderService.updateOrderStatus(order.getOrderId(), OrderStatus.DELIVERED)
        );

        // Step 2: PREPARED -> DISPATCHED
        OrderResponse s2 = orderService.updateOrderStatus(order.getOrderId(), OrderStatus.DISPATCHED);
        assertEquals(OrderStatus.DISPATCHED.getDisplayName(), s2.getStatus());

        // Step 3: DISPATCHED -> IN_TRANSIT
        OrderResponse s3 = orderService.updateOrderStatus(order.getOrderId(), OrderStatus.IN_TRANSIT);
        assertEquals(OrderStatus.IN_TRANSIT.getDisplayName(), s3.getStatus());

        // Step 4: IN_TRANSIT -> OUT_FOR_DELIVERY
        OrderResponse s4 = orderService.updateOrderStatus(order.getOrderId(), OrderStatus.OUT_FOR_DELIVERY);
        assertEquals(OrderStatus.OUT_FOR_DELIVERY.getDisplayName(), s4.getStatus());

        // Step 5: OUT_FOR_DELIVERY -> DELIVERED
        OrderResponse s5 = orderService.updateOrderStatus(order.getOrderId(), OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED.getDisplayName(), s5.getStatus());

        // Post-delivery transition or cancel must fail
        assertThrows(InvalidOperationException.class, () ->
                orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PREPARED)
        );
        assertThrows(InvalidOperationException.class, () ->
                orderService.cancelOrder(order.getOrderId())
        );
    }

    @Test
    void testOrderCancellationRestoresStock() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Cancel");
        req.setProduct("Grapes");
        req.setQuantity(30);
        req.setCity("Nuzvid");
        ProductDto product = productService.addProduct(req);

        OrderRequest orderReq = new OrderRequest();
        orderReq.setProductId(product.getProductCode());
        orderReq.setQuantity(10);
        orderReq.setDestination("Pedana");

        OrderResponse order = orderService.createOrder(orderReq);
        assertEquals(20, productService.getProductById(product.getProductCode()).getQuantity());

        // Cancel order
        OrderResponse cancelled = orderService.cancelOrder(order.getOrderId());
        assertEquals(OrderStatus.CANCELLED.getDisplayName(), cancelled.getStatus());

        // Verify stock restored
        assertEquals(30, productService.getProductById(product.getProductCode()).getQuantity());
    }
}
