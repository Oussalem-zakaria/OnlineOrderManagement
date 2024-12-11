package com.fortest.myorders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortest.myorders.bean.AuditLog;
import com.fortest.myorders.request.AuditLogRequest;
import com.fortest.myorders.service.AuditLogService;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(@RequestBody AuditLogRequest request) {
        System.out.println("Saving audit log ZIKO******2" + request.getAction());
        AuditLog auditLog = auditLogService.saveAuditLog(request);
        return ResponseEntity.ok(auditLog);
    }

    @GetMapping
    public ResponseEntity<Iterable<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAuditLogs());
    }

}
