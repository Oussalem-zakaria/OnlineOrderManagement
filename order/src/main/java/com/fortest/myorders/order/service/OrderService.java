package com.fortest.myorders.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fortest.myorders.order.beans.Order;
import com.fortest.myorders.order.beans.OrderItem;
import com.fortest.myorders.order.clients.CustomerClient;
import com.fortest.myorders.order.clients.ProductClient;
import com.fortest.myorders.order.dtos.CustomerDTO;
import com.fortest.myorders.order.dtos.OrderItemDTO;
import com.fortest.myorders.order.dtos.ProductDTO;
import com.fortest.myorders.order.dtos.ResponseDto;
import com.fortest.myorders.order.repository.OrderRepository;
import com.fortest.myorders.order.request.OrderRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final RestTemplate restTemplate;

    public OrderService(ProductClient productClient, OrderRepository orderRepository, CustomerClient customerClient,
            RestTemplate restTemplate) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.restTemplate = restTemplate;
    }

    public Order createOrder(OrderRequest orderRequest) {
        CustomerDTO customer = fetchCustomer(orderRequest.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + orderRequest.getCustomerId());
        }

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(orderItemRequest -> {
            ProductDTO product = fetchProduct(orderItemRequest.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + orderItemRequest.getProductId());
            }

            return OrderItem.builder()
                    .order(null)
                    .productId(orderItemRequest.getProductId())
                    .quantity(orderItemRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        Order order = Order.builder()
                .customerId(orderRequest.getCustomerId())
                .orderItems(orderItems)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get a specific order by ID
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public ResponseDto getOrder(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(order.getId());
        responseDto.setOrderItems(
                order.getOrderItems().stream().map(orderItem -> OrderItemDTO.builder()
                        .id(orderItem.getId())
                        .quantity(orderItem.getQuantity())
                        .products(fetchProduct(orderItem.getProductId()))
                        .build()).collect(Collectors.toList()));
        responseDto.setCustomer(fetchCustomer(order.getCustomerId()));
        return responseDto;
    }

    // Update an existing order
    public Optional<Order> updateOrder(Integer id, OrderRequest orderRequest) {
        return orderRepository.findById(id).map(order -> {
            // Update orderItems
            List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                    .map(orderItemRequest -> OrderItem.builder()
                            .order(order)
                            .productId(orderItemRequest.getProductId())
                            .quantity(orderItemRequest.getQuantity())
                            .build())
                    .collect(Collectors.toList());
            order.setOrderItems(orderItems);
            order.setCustomerId(orderRequest.getCustomerId());
            return orderRepository.save(order);
        });
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    // public ProductDTO fetchProduct(Integer productId) {
    // return productClient.getProductById(productId);
    // }
    public ProductDTO fetchProduct(Integer productId) {
        String productServiceUrl = "http://PRODUCT/api/v1/products/" + productId;
        return restTemplate.getForObject(productServiceUrl, ProductDTO.class);
    }

    // public CustomerDTO fetchCustomer(Integer customerId) {
    // return customerClient.getCustomerById(customerId);
    // }
    public CustomerDTO fetchCustomer(Integer customerId) {
        String customerServiceUrl = "http://CUSTOMER/api/v1/customers/" + customerId;
        return restTemplate.getForObject(customerServiceUrl, CustomerDTO.class);
    }

}
