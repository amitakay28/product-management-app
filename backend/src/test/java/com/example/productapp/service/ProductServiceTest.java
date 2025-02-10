package com.example.productapp.service;

import com.example.productapp.dto.ProductDTO;
import com.example.productapp.dto.ProductListDTO;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.Colour;
import com.example.productapp.model.Product;
import com.example.productapp.model.ProductType;
import com.example.productapp.repository.ColourRepository;
import com.example.productapp.repository.ProductRepository;
import com.example.productapp.repository.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ColourRepository colourRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST CASES FOR `createProduct()` **/

    @Test
    void testCreateProduct_Success() {
        ProductDTO dto = new ProductDTO("Laptop", 1L, Arrays.asList(1L, 2L));
        ProductType productType = new ProductType(1L, "Electronics");
        List<Colour> colours = Arrays.asList(new Colour(1L, "Black"), new Colour(2L, "Blue"));
        Product product = new Product(1L, "Laptop", productType, colours);

        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(colourRepository.findAllById(dto.getColourIds())).thenReturn(colours);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void testCreateProduct_ProductTypeNotFound() {
        ProductDTO dto = new ProductDTO("Laptop", 99L, Arrays.asList(1L, 2L));

        when(productTypeRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.createProduct(dto);
        });

        assertEquals("Product Type not found with ID: 99", exception.getMessage());
    }

    @Test
    void testCreateProduct_InvalidColours() {
        ProductDTO dto = new ProductDTO("Laptop", 1L, Arrays.asList(1L, 99L)); // Colour ID 99 does not exist
        ProductType productType = new ProductType(1L, "Electronics");
        List<Colour> validColours = Arrays.asList(new Colour(1L, "Black"));

        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(colourRepository.findAllById(dto.getColourIds())).thenReturn(validColours);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(dto);
        });

        assertEquals("One or more selected colours are invalid. Found: [1], Expected: [1, 99]", exception.getMessage());
    }

    @Test
    void testCreateProduct_EmptyName() {
        ProductDTO dto = new ProductDTO("", 1L, Arrays.asList(1L, 2L));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(dto);
        });

        assertEquals("Product name cannot be empty.", exception.getMessage());
    }

    @Test
    void testCreateProduct_ProductTypeIdMissing() {
        ProductDTO dto = new ProductDTO("Laptop", null, Arrays.asList(1L, 2L)); // ProductTypeId is null

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(dto);
        });

        assertEquals("Product Type ID is required.", exception.getMessage());
    }


    /** TEST CASES FOR `getProductById()` **/
    @Test
    void testGetProductById_Success() {
        Product product = new Product(1L, "Laptop", null, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });

        assertEquals("Product not found with ID: 99", exception.getMessage());
        verify(productRepository, times(1)).findById(99L);
    }


    /** TEST CASES FOR `getAllProducts()` **/

    @Test
    void testGetAllProducts_Success() {
        ProductType productType = new ProductType(1L, "Electronics");
        List<Colour> colours = Arrays.asList(new Colour(1L, "Black"), new Colour(2L, "Blue"));

        Product mockProduct = new Product(1L, "Laptop", productType, colours);
        when(productRepository.findAllByOrderByIdDesc()).thenReturn(Arrays.asList(mockProduct));

        List<ProductListDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAllProducts_NoProducts() {
        when(productRepository.findAllByOrderByIdDesc()).thenReturn(Arrays.asList());

        List<ProductListDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**  TEST CASES FOR `deleteProduct()` **/

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(99L);
        });

        assertEquals("Product not found with ID: 99", exception.getMessage());
    }

    /**  TEST CASES FOR `updateProduct()`  **/

    @Test
    void testUpdateProduct_Success() {
        ProductDTO dto = new ProductDTO("Updated Laptop", 1L, Arrays.asList(1L, 2L));
        Product existingProduct = new Product(1L, "Laptop", null, null);
        ProductType productType = new ProductType(1L, "Electronics");
        List<Colour> colours = Arrays.asList(new Colour(1L, "Black"), new Colour(2L, "Blue"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(colourRepository.findAllById(dto.getColourIds())).thenReturn(colours);
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Laptop", result.getName());
        assertEquals(productType, result.getProductType());
        assertEquals(2, result.getColours().size());
    }

    @Test
    void testUpdateProduct_NotFound() {
        ProductDTO dto = new ProductDTO("Updated Laptop", 1L, Arrays.asList(1L, 2L));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(99L, dto);
        });

        assertEquals("Product not found with ID: 99", exception.getMessage());
    }

    @Test
    void testUpdateProduct_ProductTypeNotFound() {
        ProductDTO dto = new ProductDTO("Updated Laptop", 99L, Arrays.asList(1L, 2L));
        Product existingProduct = new Product(1L, "Laptop", null, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productTypeRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(1L, dto);
        });

        assertEquals("Product Type not found", exception.getMessage());
    }

}
