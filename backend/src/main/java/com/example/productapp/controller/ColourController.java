package com.example.productapp.controller;

import com.example.productapp.model.Colour;
import com.example.productapp.service.ColourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Colours.
 */
@RestController
@RequestMapping("/api/colours")
public class ColourController {

    private static final Logger logger = LoggerFactory.getLogger(ColourController.class);

    private final ColourService colourService;

    public ColourController(ColourService colourService) {
        this.colourService = colourService;
    }

    /**
     * Retrieves all available colours.
     *
     * @return List of colours.
     */
    @GetMapping
    public ResponseEntity<List<Colour>> getAllColours() {
        logger.info("Received request to fetch all colours.");
        List<Colour> colours = colourService.getAllColours();
        logger.info("Returning {} colours in response.", colours.size());
        return ResponseEntity.ok(colours);
    }

    /**
     * Creates a new colour.
     *
     * @param colour Colour data.
     * @return The created colour.
     */
    @PostMapping
    public ResponseEntity<Colour> createColour(@RequestBody Colour colour) {
        logger.info("Received request to create a new colour: {}", colour.getName());
        Colour createdColour = colourService.createColour(colour);
        logger.info("New colour created with ID: {}", createdColour.getId());
        return ResponseEntity.ok(createdColour);
    }

    /**
     * Deletes a colour by ID.
     *
     * @param id Colour ID.
     * @return Response entity with status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColour(@PathVariable Long id) {
        logger.info("Received request to delete colour with ID: {}", id);
        colourService.deleteColour(id);
        logger.info("Successfully deleted colour with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing colour by ID.
     *
     * @param id Colour ID.
     * @param colour Updated colour details.
     * @return The updated colour entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Colour> updateColour(@PathVariable Long id, @RequestBody Colour colour) {
        logger.info("Received request to update colour with ID: {}", id);
        Colour updatedColour = colourService.updateColour(id, colour);
        logger.info("Returning updated colour with ID: {} and name: {}", id, updatedColour.getName());
        return ResponseEntity.ok(updatedColour);
    }
}
