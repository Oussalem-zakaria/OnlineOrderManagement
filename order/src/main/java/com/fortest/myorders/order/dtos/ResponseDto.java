package com.fortest.myorders.order.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private Integer id;
    // private List<ProductDTO> products;
    private List<OrderItemDTO> orderItems;
    private CustomerDTO customer;
}