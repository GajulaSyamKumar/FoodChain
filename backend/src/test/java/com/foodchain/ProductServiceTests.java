package com.foodchain;

import com.foodchain.dto.CreateProductRequest;
import com.foodchain.dto.ProductDto;
import com.foodchain.dto.UpdateStockRequest;
import com.foodchain.exception.InvalidOperationException;
import com.foodchain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    void testCreateProductAndGeneratedProductCode() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Test");
        req.setProduct("Fresh Cabbage");
        req.setQuantity(50);
        req.setCity("Vijayawada");
        req.setPrice(30.0);
        req.setDescription("Crisp local cabbage");

        ProductDto created = productService.addProduct(req);
        assertNotNull(created);
        assertNotNull(created.getProductCode());
        assertTrue(created.getProductCode() >= 356344);
        assertEquals("Fresh Cabbage", created.getProduct());
        assertEquals(50, created.getQuantity());
    }

    @Test
    void testDuplicateSameFarmerProductIncreasesStock() {
        CreateProductRequest req1 = new CreateProductRequest();
        req1.setFarmer("Farmer Ravi");
        req1.setProduct("Organic Tomatoes");
        req1.setQuantity(100);
        req1.setCity("Vijayawada");
        ProductDto p1 = productService.addProduct(req1);

        CreateProductRequest req2 = new CreateProductRequest();
        req2.setFarmer("Farmer Ravi");
        req2.setProduct("Organic Tomatoes");
        req2.setQuantity(50);
        req2.setCity("Vijayawada");
        ProductDto p2 = productService.addProduct(req2);

        assertEquals(p1.getProductCode(), p2.getProductCode());
        assertEquals(150, p2.getQuantity());
    }

    @Test
    void testSearchProductsByName() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Suresh");
        req.setProduct("Golden Wheat");
        req.setQuantity(80);
        req.setCity("Nuzvid");
        productService.addProduct(req);

        List<ProductDto> results = productService.searchProductsByName("wheat");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(p -> p.getProduct().equalsIgnoreCase("Golden Wheat")));
    }

    @Test
    void testFilterByCity() {
        List<ProductDto> list = productService.getAllProducts("Gudivada");
        assertNotNull(list);
    }

    @Test
    void testInvalidQuantityRejected() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Anitha");
        req.setProduct("Spinach");
        req.setQuantity(-5);
        req.setCity("Pedana");

        assertThrows(InvalidOperationException.class, () -> productService.addProduct(req));
    }

    @Test
    void testInvalidCityRejected() {
        CreateProductRequest req = new CreateProductRequest();
        req.setFarmer("Farmer Test City");
        req.setProduct("Fresh Corn");
        req.setQuantity(20);
        req.setCity("Mumbai"); // Outside Krishna zone

        assertThrows(InvalidOperationException.class, () -> productService.addProduct(req));
    }
}
