package com.fortest.myorders.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fortest.myorders.user.bean.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}