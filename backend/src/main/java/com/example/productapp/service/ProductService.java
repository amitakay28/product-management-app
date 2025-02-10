package com.example.productapp.service;

import com.example.productapp.dto.ProductDTO;
import com.example.productapp.dto.ProductListDTO;
import com.example.productapp.exception.CustomException;
import com.example.productapp.model.Colour;
import com.example.productapp.model.Product;
import com.example.productapp.model.ProductType;
import com.example.productapp.repository.ColourRepository;
import com.example.productapp.repository.ProductRepository;
import com.example.productapp.repository.ProductTypeRepository;
import com.example.productapp.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to handle business logic for Products.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ColourRepository colourRepository;

    public ProductService(ProductRepository productRepository, ProductTypeRepository productTypeRepository, ColourRepository colourRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.colourRepository = colourRepository;
    }

    /**
     * Creates a new product after validating the input.
     *
     * @param dto Product creation request data.
     * @return Created product.
     */
    @Transactional
    public Product createProduct(ProductDTO dto) {
        logger.info("Creating a new product: {}", dto.getName());

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            logger.error("Product creation failed: Name cannot be empty.");
            throw new IllegalArgumentException("Product name cannot be empty.");
        }

        if (dto.getProductTypeId() == null) {
            logger.error("Product creation failed: Product Type ID is required.");
            throw new IllegalArgumentException("Product Type ID is required.");
        }

        ProductType productType = productTypeRepository.findById(dto.getProductTypeId())
                .orElseThrow(() -> {
                    logger.error("Product creation failed: Product Type not found with ID: {}", dto.getProductTypeId());
                    return new ResourceNotFoundException("Product Type not found with ID: " + dto.getProductTypeId());
                });

        List<Colour> colours = colourRepository.findAllById(dto.getColourIds());
        List<Long> foundColourIds = colours.stream().map(Colour::getId).toList();

        if (foundColourIds.size() != dto.getColourIds().size()) {
            logger.error("Product creation failed: One or more selected colours are invalid. Found: {}, Expected: {}", foundColourIds, dto.getColourIds());
            throw new IllegalArgumentException("One or more selected colours are invalid. Found: " + foundColourIds + ", Expected: " + dto.getColourIds());
        }

        Product product = Product.builder()
                .name(dto.getName().trim())
                .productType(productType)
                .colours(colours)
                .build();

        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }

    /**
     * Retrieves all products, ordered by creation date (newest first).
     *
     * @return List of ProductListDTO.
     */
    public List<ProductListDTO> getAllProducts() {
        logger.info("Fetching all products...");
        List<ProductListDTO> products = productRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Fetched {} products successfully.", products.size());
        return products;
    }

    /**
     * Retrieves a product by ID or throws a custom exception if not found.
     *
     * @param id Product ID.
     * @return The found product.
     */
    public Product getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });
    }

    /**
     * Deletes a product by ID.
     *
     * @param id Product ID.
     */
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            logger.error("Delete failed: Product not found with ID: {}", id);
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        logger.info("Product with ID: {} deleted successfully.", id);
    }

    /**
     * Updates an existing product with new details including name, product type, and colours.
     *
     * @param id Product ID.
     * @param dto Data Transfer Object (DTO) containing updated product details.
     * @return The updated product entity.
     */
    @Transactional
    public Product updateProduct(Long id, ProductDTO dto) {
        logger.info("Updating product with ID: {}", id);

        // Validate required fields
        if (dto.getName().isEmpty()) {
            logger.error("Updated product name was empty");
            throw new CustomException("Please provide a product name.");
        }
        if (dto.getProductTypeId() == null) {
            logger.error("Updated product type was empty");
            throw new CustomException("Please provide a product type.");
        }
        if (dto.getColourIds() == null || dto.getColourIds().isEmpty()) {
            logger.error("Updated product colours were empty");
            throw new CustomException("Please provide at least one color.");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Update failed: Product not found with ID: {}", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        product.setName(dto.getName());

        ProductType productType = productTypeRepository.findById(dto.getProductTypeId())
                .orElseThrow(() -> {
                    logger.error("Update failed: Product Type not found.");
                    return new ResourceNotFoundException("Product Type not found");
                });

        product.setProductType(productType);

        List<Colour> colours = colourRepository.findAllById(dto.getColourIds());
        product.setColours(colours);

        Product updatedProduct = productRepository.save(product);
        logger.info("Product updated successfully: ID: {}, Name: {}", updatedProduct.getId(), updatedProduct.getName());

        return updatedProduct;
    }

    /**
     * Converts a Product entity to a ProductListDTO.
     *
     * @param product Product entity.
     * @return ProductListDTO.
     */
    private ProductListDTO convertToDTO(Product product) {
        return new ProductListDTO(
                product.getId(),
                product.getName(),
                product.getProductType().getName(),
                product.getColours().stream().map(Colour::getName).collect(Collectors.toList())
        );
    }
}
