package com.example.productapp.controller;

import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.Colour;
import com.example.productapp.service.ColourService;
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

class ColourControllerTest {

    @Mock
    private ColourService colourService;

    @InjectMocks
    private ColourController colourController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST CASES FOR `getAllColours()` **/

    @Test
    void testGetAllColours_Success() {
        List<Colour> colours = Arrays.asList(new Colour(1L, "Red"), new Colour(2L, "Blue"));
        when(colourService.getAllColours()).thenReturn(colours);

        ResponseEntity<List<Colour>> response = colourController.getAllColours();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllColours_EmptyList() {
        when(colourService.getAllColours()).thenReturn(Arrays.asList());

        ResponseEntity<List<Colour>> response = colourController.getAllColours();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    /** TEST CASES FOR `createColour()` **/

    @Test
    void testCreateColour_Success() {
        Colour colour = new Colour(1L, "Green");
        when(colourService.createColour(any(Colour.class))).thenReturn(colour);

        ResponseEntity<Colour> response = colourController.createColour(colour);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Green", response.getBody().getName());
    }

    @Test
    void testCreateColour_InvalidName() {
        Colour invalidColour = new Colour();
        invalidColour.setName("");

        when(colourService.createColour(any(Colour.class)))
                .thenThrow(new IllegalArgumentException("Colour name cannot be empty."));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            colourController.createColour(invalidColour);
        });

        assertEquals("Colour name cannot be empty.", exception.getMessage());
    }

    /** TEST CASES FOR `deleteColour()` **/

    @Test
    void testDeleteColour_Success() {
        doNothing().when(colourService).deleteColour(1L);

        ResponseEntity<Void> response = colourController.deleteColour(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(colourService, times(1)).deleteColour(1L);
    }

    @Test
    void testDeleteColour_NotFound() {
        doThrow(new ResourceNotFoundException("Colour not found with ID: 99"))
                .when(colourService).deleteColour(99L);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            colourController.deleteColour(99L);
        });

        assertEquals("Colour not found with ID: 99", exception.getMessage());
    }

    /** TEST CASES FOR `updateColour()` **/

    @Test
    void testUpdateColour_Success() {
        Colour updatedColour = new Colour(1L, "Orange");
        when(colourService.updateColour(eq(1L), any(Colour.class))).thenReturn(updatedColour);

        ResponseEntity<Colour> response = colourController.updateColour(1L, updatedColour);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Orange", response.getBody().getName());
    }

    @Test
    void testUpdateColour_NotFound() {
        Colour updatedColour = new Colour(99L, "Purple");

        when(colourService.updateColour(eq(99L), any(Colour.class)))
                .thenThrow(new ResourceNotFoundException("Colour not found with ID: 99"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            colourController.updateColour(99L, updatedColour);
        });

        assertEquals("Colour not found with ID: 99", exception.getMessage());
    }
}
