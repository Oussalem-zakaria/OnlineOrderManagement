package com.fortest.myorders.order.beans;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; 

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

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<OrderItem> orderItems;

        private String status;
        private Double total_price;
        // la date de commande auto générée
        @Column(updatable = false) // La date ne sera pas mise à jour après la création
        private LocalDateTime orderDate;

        @PrePersist // Méthode exécutée avant l'insertion en base
        protected void onCreate() {
                orderDate = LocalDateTime.now();
        }

}
