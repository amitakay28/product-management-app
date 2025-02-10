package com.example.productapp.controller;

import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.ProductType;
import com.example.productapp.service.ProductTypeService;
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

class ProductTypeControllerTest {

    @Mock
    private ProductTypeService productTypeService;

    @InjectMocks
    private ProductTypeController productTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST CASES FOR `getAllProductTypes()` **/

    @Test
    void testGetAllProductTypes_Success() {
        List<ProductType> productTypes = Arrays.asList(new ProductType(1L, "Electronics"), new ProductType(2L, "Clothing"));
        when(productTypeService.getAllProductTypes()).thenReturn(productTypes);

        ResponseEntity<List<ProductType>> response = productTypeController.getAllProductTypes();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllProductTypes_EmptyList() {
        when(productTypeService.getAllProductTypes()).thenReturn(Arrays.asList());

        ResponseEntity<List<ProductType>> response = productTypeController.getAllProductTypes();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    /** TEST CASES FOR `createProductType()` **/

    @Test
    void testCreateProductType_Success() {
        ProductType productType = new ProductType(1L, "Appliances");
        when(productTypeService.createProductType(any(ProductType.class))).thenReturn(productType);

        ResponseEntity<ProductType> response = productTypeController.createProductType(productType);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Appliances", response.getBody().getName());
    }

    @Test
    void testCreateProductType_InvalidName() {
        ProductType invalidType = new ProductType();
        invalidType.setName("");

        when(productTypeService.createProductType(any(ProductType.class)))
                .thenThrow(new IllegalArgumentException("Product Type name cannot be empty."));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productTypeController.createProductType(invalidType);
        });

        assertEquals("Product Type name cannot be empty.", exception.getMessage());
    }

    /** TEST CASES FOR `deleteProductType()` **/

    @Test
    void testDeleteProductType_Success() {
        doNothing().when(productTypeService).deleteProductType(1L);

        ResponseEntity<Void> response = productTypeController.deleteProductType(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productTypeService, times(1)).deleteProductType(1L);
    }

    @Test
    void testDeleteProductType_NotFound() {
        doThrow(new ResourceNotFoundException("Product Type not found with ID: 99"))
                .when(productTypeService).deleteProductType(99L);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productTypeController.deleteProductType(99L);
        });

        assertEquals("Product Type not found with ID: 99", exception.getMessage());
    }

    /** TEST CASES FOR `updateProductType()` **/

    @Test
    void testUpdateProductType_Success() {
        ProductType updatedType = new ProductType(1L, "Furniture");
        when(productTypeService.updateProductType(eq(1L), any(ProductType.class))).thenReturn(updatedType);

        ResponseEntity<ProductType> response = productTypeController.updateProductType(1L, updatedType);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Furniture", response.getBody().getName());
    }

    @Test
    void testUpdateProductType_NotFound() {
        ProductType updatedType = new ProductType(99L, "Toys");

        when(productTypeService.updateProductType(eq(99L), any(ProductType.class)))
                .thenThrow(new ResourceNotFoundException("Product Type not found with ID: 99"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productTypeController.updateProductType(99L, updatedType);
        });

        assertEquals("Product Type not found with ID: 99", exception.getMessage());
    }
}
