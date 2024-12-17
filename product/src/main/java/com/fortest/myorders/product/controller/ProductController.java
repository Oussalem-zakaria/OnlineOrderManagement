package com.fortest.myorders.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import com.fortest.myorders.product.bean.Product;
import com.fortest.myorders.product.request.ProductRequest;
import com.fortest.myorders.product.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Value("${upload.directory}") // Définissez un répertoire de stockage dans application.properties
    private String uploadDirectory;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();  // Get the product from the Optional
            byte[] imageFile = product.getImageData();  // Access the image data
            return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // @PostMapping(consumes = {"multipart/form-data"})
    // @PreAuthorize("hasAuthority('ADMIN')")
    // public ResponseEntity<Product> createProduct(
    //         @RequestParam("name") String name,
    //         @RequestParam("description") String description,
    //         @RequestParam("price") Double price,
    //         @RequestParam("stock_quantity") Integer stockQuantity,
    //         @RequestParam(value = "image", required = false) MultipartFile image) {

    //     // Log pour le débogage
    //     log.info("New product registration - Name: {}, Price: {}", name, price);

    //     // Construire un objet `ProductRequest` manuellement
    //     ProductRequest productRequest = new ProductRequest();
    //     productRequest.setName(name);
    //     productRequest.setDescription(description);
    //     productRequest.setPrice(price);
    //     productRequest.setStock_quantity(stockQuantity);

    //     // Appeler le service pour gérer l'image et sauvegarder le produit
    //     Product newProduct = productService.createProduct(productRequest, image);

    //     return ResponseEntity.status(201).body(newProduct);
    // }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<Product> getProduct() {
        log.info("get all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    // public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
    //     return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    // }

    @PutMapping("/{id}/reduce-stock")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> reduceStock(@PathVariable("id") Integer id,
            @RequestParam("quantity") Integer quantity) {
        // Utilisation de Optional pour récupérer le produit
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Vérification de la quantité en stock
        if (product.getStock_quantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Mise à jour des informations du produit dans ProductRequest
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(product.getName());
        productRequest.setDescription(product.getDescription());
        productRequest.setPrice(product.getPrice());
        productRequest.setStock_quantity(product.getStock_quantity() - quantity);

        // Appel à updateProduct avec les paramètres attendus
        productService.update(id, productRequest);

        return ResponseEntity.ok().build();
    }

    // Delete a product by ID
    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    // public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
    //     productService.deleteProduct(id);
    //     return ResponseEntity.noContent().build();
    // }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> updateProduct(
        @PathVariable int id, 
        @RequestPart("product") Product product, 
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            Product updatedProduct = productService.updateProduct(id, product, imageFile);
            return ResponseEntity.ok("Produit mis à jour avec succès");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Échec de la mise à jour");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String filePath = uploadDirectory + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(filePath));
            return ResponseEntity.ok("Image uploaded successfully: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload image");
        }
    }

}