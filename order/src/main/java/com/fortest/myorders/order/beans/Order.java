package com.fortest.myorders.order.beans;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

        @Id
        @SequenceGenerator(name = "order_id_sequence", sequenceName = "order_id_sequence")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_sequence")
        private Integer id;

        private Integer customerId;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<OrderItem> orderItems;

        private String status;
        private Double total_price;
        // la date de commande auto générée
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private String orderDate;

}
