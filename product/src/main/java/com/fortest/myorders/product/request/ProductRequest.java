package com.fortest.myorders.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    // private Integer id;
    private String name;
    private String description;
    private double price;
    private Integer stock_quantity;
    private String category;
}
