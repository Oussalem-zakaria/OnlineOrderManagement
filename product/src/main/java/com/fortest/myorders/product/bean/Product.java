package com.fortest.myorders.product.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {

        @Id
        @SequenceGenerator(name = "product_id_sequence", sequenceName = "product_id_sequence")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_sequence")
        private Integer id;
        private String name;
        private String description;
        private Double price;
        private Integer stock_quantity;
        private String category;
        
        private String imageName;
        private String imageType;
        @Lob
        private byte[] imageData;
}
