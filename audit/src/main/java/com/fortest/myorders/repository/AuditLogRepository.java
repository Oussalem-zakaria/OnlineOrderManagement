package com.fortest.myorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fortest.myorders.bean.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
