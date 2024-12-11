package com.fortest.myorders.product.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.fortest.myorders.product.bean.Product;
import com.fortest.myorders.product.repository.ProductRepository;
import com.fortest.myorders.product.request.ProductRequest;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest) {
        Product customer = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        return productRepository.saveAndFlush(customer);

    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Integer id, ProductRequest productRequest) {
        return productRepository.findById(id).map(product -> {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock_quantity(productRequest.getStock_quantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete a product by ID
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
