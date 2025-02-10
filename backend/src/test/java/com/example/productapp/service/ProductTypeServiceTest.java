package com.example.productapp.service;

import com.example.productapp.exception.CustomException;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.ProductType;
import com.example.productapp.repository.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductTypeService productTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST CASES FOR `getAllProductTypes()` **/
    @Test
    void testGetAllProductTypes_Success() {
        List<ProductType> productTypes = Arrays.asList(new ProductType(1L, "Electronics"), new ProductType(2L, "Clothing"));
        when(productTypeRepository.findAll()).thenReturn(productTypes);

        List<ProductType> result = productTypeService.getAllProductTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /** TEST CASES FOR `createProductType()` **/
    @Test
    void testCreateProductType_Success() {
        ProductType productType = new ProductType(1L, "Electronics");

        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType result = productTypeService.createProductType(productType);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(productTypeRepository, times(1)).save(any(ProductType.class));
    }

    @Test
    void testCreateProductType_EmptyName() {
        ProductType productType = new ProductType();
        productType.setName(""); // Empty name case

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productTypeService.createProductType(productType);
        });

        assertEquals("Product Type name cannot be empty.", exception.getMessage());
        verify(productTypeRepository, never()).save(any(ProductType.class));
    }


    /** TEST CASES FOR `deleteProductType()` **/
    @Test
    void testDeleteProductType_Success() {
        when(productTypeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productTypeRepository).deleteById(1L);

        assertDoesNotThrow(() -> productTypeService.deleteProductType(1L));
    }

    @Test
    void testDeleteProductType_NotFound() {
        when(productTypeRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> productTypeService.deleteProductType(99L));
        assertEquals("Product Type not found with ID: 99", exception.getMessage());
    }

    @Test
    void testDeleteProductType_ConstraintViolation() {
        when(productTypeRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(productTypeRepository).deleteById(1L);

        Exception exception = assertThrows(CustomException.class, () -> productTypeService.deleteProductType(1L));
        assertEquals("Cannot delete product type. It is being used in existing products.", exception.getMessage());
    }

    /** TEST CASES FOR `updateProductType()` **/

    @Test
    void testUpdateProductType_Success() {
        ProductType existing = new ProductType(1L, "Home Decor");
        ProductType updated = new ProductType(1L, "Furniture");

        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(updated);

        ProductType result = productTypeService.updateProductType(1L, updated);

        assertNotNull(result);
        assertEquals("Furniture", result.getName());
    }

    @Test
    void testUpdateProductType_NotFound() {
        when(productTypeRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> productTypeService.updateProductType(99L, new ProductType()));
        assertEquals("Product Type not found with ID: 99", exception.getMessage());
    }
}
