package com.fortest.myorders.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fortest.myorders.order.config.FeignClientConfig;
import com.fortest.myorders.order.dtos.CustomerDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "CUSTOMER", url = "http://customer-service:8081", configuration = FeignClientConfig.class)
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}")
    @CircuitBreaker(name = "customer", fallbackMethod = "getDefaultCustomer")
    public CustomerDTO getCustomerById(@PathVariable("id") Integer id);

    default CustomerDTO getDefaultCustomer(Integer id, Exception e) {
        return new CustomerDTO(
                id,
                "Customer not found",
                "Customer not found",
                "Customer not found");
    }
}