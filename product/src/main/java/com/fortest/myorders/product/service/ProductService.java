package com.fortest.myorders.product.service;

import com.fortest.myorders.product.bean.Product;
import com.fortest.myorders.product.repository.ProductRepository;
import com.fortest.myorders.product.request.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Chemin pour sauvegarder les images localement
    // private final String uploadDir = "uploads/";

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return productRepository.save(product);
    }

    // public Product createProduct(ProductRequest productRequest, MultipartFile image) {
    //     String imagePath = null;

    //     // Gérer la sauvegarde de l'image si elle existe
    //     if (image != null && !image.isEmpty()) {
    //         imagePath = saveImage(image);
    //     }

    //     // Construire l'objet Product
    //     Product product = Product.builder()
    //             .name(productRequest.getName())
    //             .description(productRequest.getDescription())
    //             .price(productRequest.getPrice())
    //             .stock_quantity(productRequest.getStock_quantity())
    //             .imagePath(imagePath) // Assigner le chemin de l'image
    //             .build();

    //     // Sauvegarder le produit dans la base de données
    //     return productRepository.saveAndFlush(product);
    // }

    // // Méthode pour sauvegarder l'image dans un dossier local
    // private String saveImage(MultipartFile image) {
    //     try {
    //         // Créer un nom de fichier unique pour éviter les conflits
    //         String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

    //         // Créer le chemin complet pour sauvegarder l'image
    //         Path filePath = Paths.get(uploadDir + fileName);

    //         // Créer le dossier si nécessaire
    //         if (!Files.exists(filePath.getParent())) {
    //             Files.createDirectories(filePath.getParent());
    //         }

    //         // Sauvegarder le fichier
    //         Files.copy(image.getInputStream(), filePath);

    //         // Retourner le chemin relatif ou absolu
    //         return filePath.toString();
    //     } catch (IOException e) {
    //         throw new RuntimeException("Erreur lors de la sauvegarde de l'image", e);
    //     }
    // }

    // Récupérer tous les produits
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Récupérer un produit par ID
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    // // Mettre à jour un produit existant
    public Product update(Integer id, ProductRequest productRequest) {
        return productRepository.findById(id).map(product -> {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock_quantity(productRequest.getStock_quantity());
            product.setCategory(productRequest.getCategory());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // // Supprimer un produit par ID
    // public void deleteProduct(Integer id) {
    //     productRepository.deleteById(id);
    // }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        // Chercher le produit existant
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Mettre à jour les champs du produit existant
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock_quantity(product.getStock_quantity());
        existingProduct.setCategory(product.getCategory());

        // Mise à jour de l'image si un nouveau fichier est fourni
        if (imageFile != null && !imageFile.isEmpty()) {
            existingProduct.setImageName(imageFile.getOriginalFilename());
            existingProduct.setImageType(imageFile.getContentType());
            existingProduct.setImageData(imageFile.getBytes());
        }

        // Sauvegarder et retourner le produit mis à jour
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
