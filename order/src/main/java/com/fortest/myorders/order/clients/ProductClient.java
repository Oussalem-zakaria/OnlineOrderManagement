package com.fortest.myorders.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fortest.myorders.order.config.FeignClientConfig;
import com.fortest.myorders.order.dtos.ProductDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "PRODUCT", url = "http://product-service:8082", configuration = FeignClientConfig.class)
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    @CircuitBreaker(name = "product", fallbackMethod = "getDefaultProduct")
    public ProductDTO getProductById(@PathVariable("id") Integer id);

    @PutMapping("/api/v1/products/{id}/reduce-stock")
    void updateProductStock(@PathVariable("id") Integer productId, @RequestParam("quantity") Integer quantity);

    default ProductDTO getDefaultProduct(Integer id, Exception e) {
        return new ProductDTO(
                id,
                "Product not found",
                "Product not found",
                0.0,
                0);
    }
}