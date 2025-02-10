package com.example.productapp.controller;

import com.example.productapp.model.ProductType;
import com.example.productapp.service.ProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Product Types.
 */
@RestController
@RequestMapping("/api/product-types")
public class ProductTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ProductTypeController.class);

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * Retrieves all product types.
     *
     * @return List of product types.
     */
    @GetMapping
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        logger.info("Request received to fetch all product types...");
        List<ProductType> productTypes = productTypeService.getAllProductTypes();
        logger.info("Returning fetched {} product types in response.", productTypes.size());
        return ResponseEntity.ok(productTypes);
    }

    /**
     * Creates a new product type.
     *
     * @param productType Product type data.
     * @return The created product type.
     */
    @PostMapping
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) {
        logger.info("Request received to create product type: {}", productType.getName());
        ProductType createdType = productTypeService.createProductType(productType);
        logger.info("Product type created successfully with ID: {}", createdType.getId());
        return ResponseEntity.ok(createdType);
    }

    /**
     * Deletes a product type by ID.
     *
     * @param id Product type ID.
     * @return Response entity with status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        logger.warn("Request received to delete product type with ID: {}", id);
        productTypeService.deleteProductType(id);
        logger.info("Product type with ID: {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing product type by ID.
     *
     * @param id Product Type ID.
     * @param productType Updated product type details.
     * @return The updated product type entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable Long id, @RequestBody ProductType productType) {
        logger.info("Request received to update product type with ID: {}", id);
        ProductType updatedType = productTypeService.updateProductType(id, productType);
        logger.info("Product type with ID: {} updated successfully to: {}", id, updatedType.getName());
        return ResponseEntity.ok(updatedType);
    }
}
