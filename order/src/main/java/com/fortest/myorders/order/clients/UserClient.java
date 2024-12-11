package com.fortest.myorders.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fortest.myorders.order.dtos.UserDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(value = "User", url = "http://localhost:8081")
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    @CircuitBreaker(name = "user", fallbackMethod = "getDefaultUser")
    public UserDTO getUserById(@PathVariable("id") Integer id);

    default UserDTO getDefaultUser(Integer id, Exception e) {
        return new UserDTO(
                id,
                "User not found",
                "User not found",
                "User not found");
    }
}