package com.example.productapp.service;

import com.example.productapp.exception.CustomException;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.ProductType;
import com.example.productapp.repository.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Product Types.
 */
@Service
public class ProductTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ProductTypeService.class);

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    /**
     * Retrieves all product types.
     *
     * @return List of product types.
     */
    public List<ProductType> getAllProductTypes() {
        logger.info("Fetching all product types from the database...");
        List<ProductType> productTypes = productTypeRepository.findAll();
        logger.info("Fetched {} product types successfully.", productTypes.size());
        return productTypes;
    }

    /**
     * Creates a new product type.
     *
     * @param productType Product type data.
     * @return The created product type.
     */
    @Transactional
    public ProductType createProductType(ProductType productType) {
        logger.info("Attempting to create a new product type: {}", productType.getName());

        if (productType.getName() == null || productType.getName().trim().isEmpty()) {
            logger.error("Failed to create product type: Name is empty or null.");
            throw new IllegalArgumentException("Product Type name cannot be empty.");
        }

        ProductType createdType = productTypeRepository.save(productType);
        logger.info("Product type created successfully with ID: {}", createdType.getId());
        return createdType;
    }

    /**
     * Deletes a product type by ID.
     *
     * @param id Product type ID.
     */
    @Transactional
    public void deleteProductType(Long id) {
        logger.warn("Deleting product type with ID: {}", id);

        if (!productTypeRepository.existsById(id)) {
            logger.error("Deletion failed: Product type with ID {} not found.", id);
            throw new ResourceNotFoundException("Product Type not found with ID: " + id);
        }

        try {
            productTypeRepository.deleteById(id);
            productTypeRepository.flush();
            logger.info("Product type with ID: {} deleted successfully.", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Cannot delete product type with ID {}. It is assigned to existing products.", id);
            throw new CustomException("Cannot delete product type. It is being used in existing products.");
        }
    }

    /**
     * Updates an existing product type by ID.
     *
     * @param id Product type ID.
     * @param updatedType The new product type details.
     * @return The updated product type entity.
     * @throws ResourceNotFoundException If the product type with the given ID does not exist.
     */
    @Transactional
    public ProductType updateProductType(Long id, ProductType updatedType) {
        logger.info("Updating product type with ID: {}", id);

        // Validate that the product type name is not empty
        if (updatedType.getName() == null || updatedType.getName().isEmpty()) {
            logger.error("Updated product type name was empty");
            throw new CustomException("Please provide a product type name.");
        }

        ProductType type = productTypeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Update failed: Product type with ID {} not found.", id);
                    return new ResourceNotFoundException("Product Type not found with ID: " + id);
                });

        type.setName(updatedType.getName());
        ProductType updatedProductType = productTypeRepository.save(type);

        logger.info("Product type with ID: {} updated successfully to: {}", id, updatedProductType.getName());
        return updatedProductType;
    }
}
