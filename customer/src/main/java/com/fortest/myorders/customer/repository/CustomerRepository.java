package com.fortest.myorders.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fortest.myorders.customer.bean.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}