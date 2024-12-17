package com.fortest.myorders.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fortest.myorders.order.beans.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(Integer customerId);
}