package com.example.productapp.service;

import com.example.productapp.exception.CustomException;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.Colour;
import com.example.productapp.repository.ColourRepository;
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

class ColourServiceTest {

    @Mock
    private ColourRepository colourRepository;

    @InjectMocks
    private ColourService colourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** TEST CASES FOR `getAllColours()` **/

    @Test
    void testGetAllColours_Success() {
        List<Colour> colours = Arrays.asList(new Colour(1L, "Red"), new Colour(2L, "Blue"));
        when(colourRepository.findAll()).thenReturn(colours);

        List<Colour> result = colourService.getAllColours();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /** TEST CASES FOR `createColour()` **/

    @Test
    void testCreateColour_Success() {
        Colour colour = new Colour(1L, "Green");
        when(colourRepository.save(any(Colour.class))).thenReturn(colour);

        Colour result = colourService.createColour(colour);

        assertNotNull(result);
        assertEquals("Green", result.getName());
    }

    @Test
    void testCreateColour_InvalidName() {
        Colour colour = new Colour();
        colour.setName("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> colourService.createColour(colour));
        assertEquals("Colour name cannot be empty.", exception.getMessage());
    }

    /** TEST CASES FOR `deleteColour()` **/

    @Test
    void testDeleteColour_Success() {
        when(colourRepository.existsById(1L)).thenReturn(true);
        doNothing().when(colourRepository).deleteById(1L);

        assertDoesNotThrow(() -> colourService.deleteColour(1L));
    }

    @Test
    void testDeleteColour_NotFound() {
        when(colourRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> colourService.deleteColour(99L));
        assertEquals("Colour not found with ID: 99", exception.getMessage());
    }

    @Test
    void testDeleteColour_ConstraintViolation() {
        when(colourRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(colourRepository).deleteById(1L);

        Exception exception = assertThrows(CustomException.class, () -> colourService.deleteColour(1L));
        assertEquals("Cannot delete colour. It is assigned to active products. Please remove it from those products first.", exception.getMessage());
    }

    /** TEST CASES FOR `updateColour()` **/

    @Test
    void testUpdateColour_Success() {
        Colour existing = new Colour(1L, "Yellow");
        Colour updated = new Colour(1L, "Orange");

        when(colourRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(colourRepository.save(any(Colour.class))).thenReturn(updated);

        Colour result = colourService.updateColour(1L, updated);

        assertNotNull(result);
        assertEquals("Orange", result.getName());
    }

    @Test
    void testUpdateColour_NotFound() {
        when(colourRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> colourService.updateColour(99L, new Colour()));
        assertEquals("Colour not found with ID: 99", exception.getMessage());
    }
}
