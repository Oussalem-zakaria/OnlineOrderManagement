package com.fortest.myorders.order.request;

import lombok.Data;

@Data
public class AuditLogRequest {
    private String userId;
    private String action;
    private String entityType;
    private String entityId;
}
