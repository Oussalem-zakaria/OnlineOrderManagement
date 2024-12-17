package com.fortest.myorders.order.service;

import org.springframework.stereotype.Service;

import com.fortest.myorders.order.beans.Order;
import com.fortest.myorders.order.beans.OrderItem;
import com.fortest.myorders.order.clients.AuditServiceClient;
import com.fortest.myorders.order.clients.CustomerClient;
import com.fortest.myorders.order.clients.ProductClient;
import com.fortest.myorders.order.dtos.CustomerDTO;
import com.fortest.myorders.order.dtos.OrderItemDTO;
import com.fortest.myorders.order.dtos.ProductDTO;
import com.fortest.myorders.order.dtos.ResponseDto;
import com.fortest.myorders.order.repository.OrderRepository;
import com.fortest.myorders.order.request.AuditLogRequest;
import com.fortest.myorders.order.request.OrderRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final AuditServiceClient auditServiceClient;

    public OrderService(ProductClient productClient, OrderRepository orderRepository, CustomerClient customerClient,
            AuditServiceClient auditServiceClient) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.auditServiceClient = auditServiceClient;
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

            if (orderItemRequest.getQuantity() > product.getStock_quantity()) {
                throw new RuntimeException("Product quantity not available");
            }

            return OrderItem.builder()
                    .order(null)
                    .productId(orderItemRequest.getProductId())
                    .quantity(orderItemRequest.getQuantity())
                    .price(orderItemRequest.getPrice())
                    .build();
        }).collect(Collectors.toList());

        // Créer et sauvegarder la commande
        Order order = Order.builder()
            .customerId(orderRequest.getCustomerId())
            .orderItems(orderItems)
            .status("En attente")
            .total_price(orderRequest.getTotal_price())
            .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        // Mettre à jour les stocks des produits
        orderItems.forEach(orderItem -> {
            try {
                productClient.updateProductStock(orderItem.getProductId(), orderItem.getQuantity());
            } catch (Exception e) {
                throw new RuntimeException("Failed to update product stock for Product ID: " + orderItem.getProductId(),
                        e);
            }
        });

        // Process the order (audit log entry)
        processOrder(savedOrder);

        return savedOrder;
    }

    public void processOrder(Order order) {
        try {
            // Préparer la requête pour AuditService
            AuditLogRequest auditLogRequest = new AuditLogRequest();
            auditLogRequest.setUserId(order.getCustomerId().toString());
            auditLogRequest.setAction("CREATE_ORDER");
            auditLogRequest.setEntityType("Order");
            auditLogRequest.setEntityId(order.getId().toString());

            // Appeler le service d'audit via Feign
            System.out.println("Saving audit log ZIKO******1" + auditLogRequest.getAction());
            auditServiceClient.createAuditLog(auditLogRequest);
        } catch (Exception e) {
            // En cas d'erreur, afficher un message dans les logs
            System.err.println("Erreur lors de l'enregistrement dans le service d'audit : " + e.getMessage());
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    // Récupérer les commandes par customerId
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        // Vérification si le client existe via CustomerClient
        CustomerDTO customer = fetchCustomer(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }

        // Retourner la liste des commandes pour ce client
        return orderRepository.findByCustomerId(customerId);
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

    public Optional<Order> updateOrder(Integer id, OrderRequest orderRequest) {
        return orderRepository.findById(id).map(order -> {
            // Mettre à jour les propriétés de la commande
            order.setCustomerId(orderRequest.getCustomerId());
            order.setTotal_price(orderRequest.getTotal_price());
            order.setStatus(orderRequest.getStatus());

            // Mettre à jour les orderItems
            List<OrderItem> existingOrderItems = order.getOrderItems();

            // Effacer les orderItems obsolètes
            existingOrderItems.clear();

            // Ajouter les nouveaux orderItems
            orderRequest.getOrderItems().forEach(orderItemRequest -> {
                OrderItem orderItem = OrderItem.builder()
                        .order(order) // Lier à la commande actuelle
                        .productId(orderItemRequest.getProductId())
                        .quantity(orderItemRequest.getQuantity())
                        .price(orderItemRequest.getPrice())
                        .build();
                existingOrderItems.add(orderItem);
            });

            return orderRepository.save(order);
        });
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    public ProductDTO fetchProduct(Integer productId) {
        return productClient.getProductById(productId);
    }

    public CustomerDTO fetchCustomer(Integer customerId) {
        return customerClient.getCustomerById(customerId);
    }
}
