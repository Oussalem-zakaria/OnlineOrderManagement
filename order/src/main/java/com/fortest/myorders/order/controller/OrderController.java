package com.fortest.myorders.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;

import com.fortest.myorders.order.beans.Order;
import com.fortest.myorders.order.dtos.ResponseDto;
import com.fortest.myorders.order.request.OrderRequest;
import com.fortest.myorders.order.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order newOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(201).body(newOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
    // Optional<Order> order = orderService.getOrderById(id);
    // return order.map(ResponseEntity::ok).orElseGet(() ->
    // ResponseEntity.notFound().build());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getOrder(@PathVariable Integer id) {
        ResponseDto responseDto = orderService.getOrder(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody OrderRequest orderRequest) {
        Optional<Order> updatedOrder = orderService.updateOrder(id, orderRequest);
        return updatedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}