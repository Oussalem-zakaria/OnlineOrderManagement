package com.fortest.myorders.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fortest.myorders.bean.AuditLog;
import com.fortest.myorders.repository.AuditLogRepository;
import com.fortest.myorders.request.AuditLogRequest;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog saveAuditLog(AuditLogRequest request) {
        System.out.println("Saving audit log ZIKO******3" + request.getAction());
        AuditLog auditLog = AuditLog.builder()
                .userId(request.getUserId())
                .action(request.getAction())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .timestamp(LocalDateTime.now())
                .build();
        return auditLogRepository.save(auditLog);
    }

    public Iterable<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }

}
