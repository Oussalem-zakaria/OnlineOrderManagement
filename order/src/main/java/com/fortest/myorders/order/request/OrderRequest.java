package com.fortest.myorders.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Integer customerId;
    private List<OrderItemRequest> orderItems;
    private Double total_price;
    private String orderDate;
    private String status;
}