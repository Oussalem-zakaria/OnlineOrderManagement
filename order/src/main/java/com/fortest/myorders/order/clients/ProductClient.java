package com.fortest.myorders.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fortest.myorders.order.dtos.ProductDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "PRODUCT", url = "http://localhost:8082")
public interface ProductClient {
    @GetMapping("/api/v1/products/{id}")
    @CircuitBreaker(name = "product", fallbackMethod = "getDefaultProduct")
    public ProductDTO getProductById(@PathVariable("id") Integer id);

    default ProductDTO getDefaultProduct(Integer id, Exception e) {
        return new ProductDTO(
                id,
                "Product not found",
                "Product not found",
                0.0);
    }
}