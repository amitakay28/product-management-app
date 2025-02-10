package com.example.productapp.controller;

import com.example.productapp.dto.ProductDTO;
import com.example.productapp.dto.ProductListDTO;
import com.example.productapp.model.Product;
import com.example.productapp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Product-related API endpoints.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product.
     *
     * @param dto Product creation request data.
     * @return ResponseEntity with the created product.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO dto) {
        logger.info("Received request to create a new product: {}", dto.getName());
        Product createdProduct = productService.createProduct(dto);
        logger.info("Product created successfully with ID: {}", createdProduct.getId());
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * Retrieves all products with detailed information, ordered by creation date.
     *
     * @return List of products.
     */
    @GetMapping
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        logger.info("Received request to fetch all products.");
        List<ProductListDTO> products = productService.getAllProducts();
        logger.info("Returning {} products in response.", products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id Product ID.
     * @return ResponseEntity with the found product.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Received request to fetch product with ID: {}", id);
        Product product = productService.getProductById(id);
        logger.info("Returning product with ID: {} and name: {}", product.getId(), product.getName());
        return ResponseEntity.ok(product);
    }

    /**
     * Deletes a product by ID.
     *
     * @param id Product ID.
     * @return Response entity with status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with ID: {}", id);
        productService.deleteProduct(id);
        logger.info("Successfully deleted product with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing product.
     *
     * @param id  Product ID.
     * @param dto Updated product details.
     * @return Updated product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        logger.info("Received request to update product with ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, dto);
        logger.info("Returning updated product with ID: {} and name: {}", updatedProduct.getId(), updatedProduct.getName());
        return ResponseEntity.ok(updatedProduct);
    }
}
