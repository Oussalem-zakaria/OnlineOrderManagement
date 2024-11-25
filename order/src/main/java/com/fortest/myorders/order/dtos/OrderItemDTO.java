package com.fortest.myorders.order.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private Integer id;
    private ProductDTO products;
    private Double quantity;
}