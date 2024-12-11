package com.fortest.myorders.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fortest.myorders.order.config.FeignClientConfig;
import com.fortest.myorders.order.request.AuditLogRequest;

@FeignClient(value = "AUDIT", url = "http://localhost:8084", configuration = FeignClientConfig.class)
public interface AuditServiceClient {

    @PostMapping("/api/v1/audit")
    void createAuditLog(@RequestBody AuditLogRequest auditLogRequest);

}
