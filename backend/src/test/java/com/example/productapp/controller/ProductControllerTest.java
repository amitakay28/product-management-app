package com.example.productapp.controller;

import com.example.productapp.dto.ProductDTO;
import com.example.productapp.dto.ProductListDTO;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.Product;
import com.example.productapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test successful product creation
     */
    @Test
    void testCreateProduct_Success() {
        ProductDTO dto = new ProductDTO("Laptop", 1L, Arrays.asList(1L, 2L));
        Product product = new Product(1L, "Laptop", null, null);

        when(productService.createProduct(dto)).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getName());
    }

    /**
     * Test product creation with invalid input
     */
    @Test
    void testCreateProduct_InvalidInput() {
        ProductDTO dto = new ProductDTO("", null, Arrays.asList(1L, 2L));

        when(productService.createProduct(dto)).thenThrow(new IllegalArgumentException("Invalid product details"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productController.createProduct(dto);
        });

        assertEquals("Invalid product details", exception.getMessage());
    }

    /**
     * Test retrieving all products successfully
     */
    @Test
    void testGetAllProducts_Success() {
        List<ProductListDTO> products = Arrays.asList(new ProductListDTO(1L, "Laptop", "Electronics", Arrays.asList("Black, Blue")));
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductListDTO>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    /**
     * Test getting a product by ID successfully
     */
    @Test
    void testGetProductById_Success() {
        Product product = new Product(1L, "Laptop", null, null);
        when(productService.getProductById(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getName());
    }

    /**
     * Test retrieving a non-existent product (should throw exception)
     */
    @Test
    void testGetProductById_NotFound() {
        when(productService.getProductById(99L)).thenThrow(new ResourceNotFoundException("Product not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.getProductById(99L);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    /**
     * Test deleting a product successfully
     */
    @Test
    void testDeleteProduct_Success() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).deleteProduct(1L);
    }

    /**
     * Test deleting a non-existent product (should throw exception)
     */
    @Test
    void testDeleteProduct_NotFound() {
        doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteProduct(99L);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.deleteProduct(99L);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    /**
     * Test updating a product successfully
     */
    @Test
    void testUpdateProduct_Success() {
        ProductDTO dto = new ProductDTO("Updated Laptop", 1L, Arrays.asList(1L, 2L));
        Product updatedProduct = new Product(1L, "Updated Laptop", null, null);

        when(productService.updateProduct(1L, dto)).thenReturn(updatedProduct);

        ResponseEntity<Product> response = productController.updateProduct(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Laptop", response.getBody().getName());
    }

    /**
     * Test updating a non-existent product (should throw exception)
     */
    @Test
    void testUpdateProduct_NotFound() {
        ProductDTO dto = new ProductDTO("Updated Laptop", 1L, Arrays.asList(1L, 2L));

        when(productService.updateProduct(99L, dto)).thenThrow(new ResourceNotFoundException("Product not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productController.updateProduct(99L, dto);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
