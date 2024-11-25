package com.fortest.myorders.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fortest.myorders.product.bean.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
