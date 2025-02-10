package com.example.productapp.service;

import com.example.productapp.exception.CustomException;
import com.example.productapp.exception.ResourceNotFoundException;
import com.example.productapp.model.Colour;
import com.example.productapp.repository.ColourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Colours.
 */
@Service
public class ColourService {

    private static final Logger logger = LoggerFactory.getLogger(ColourService.class);

    private final ColourRepository colourRepository;

    public ColourService(ColourRepository colourRepository) {
        this.colourRepository = colourRepository;
    }

    /**
     * Retrieves all available colours.
     *
     * @return List of colours.
     */
    public List<Colour> getAllColours() {
        logger.info("Fetching all colours...");
        List<Colour> colours = colourRepository.findAll();
        logger.info("Retrieved {} colours successfully.", colours.size());
        return colours;
    }

    /**
     * Creates a new colour.
     *
     * @param colour Colour data.
     * @return The created colour.
     */
    @Transactional
    public Colour createColour(Colour colour) {
        logger.info("Creating a new colour: {}", colour.getName());

        if (colour.getName() == null || colour.getName().trim().isEmpty()) {
            logger.error("Failed to create colour: Colour name is empty.");
            throw new IllegalArgumentException("Colour name cannot be empty.");
        }

        Colour createdColour = colourRepository.save(colour);
        logger.info("Colour created successfully with ID: {}", createdColour.getId());
        return createdColour;
    }

    /**
     * Deletes a colour by ID.
     *
     * @param id Colour ID.
     */
    @Transactional
    public void deleteColour(Long id) {
        logger.warn("Attempting to delete colour with ID: {}", id);

        if (!colourRepository.existsById(id)) {
            logger.error("Failed to delete colour: Colour with ID {} not found.", id);
            throw new ResourceNotFoundException("Colour not found with ID: " + id);
        }

        try {
            colourRepository.deleteById(id);
            colourRepository.flush();
            logger.info("Colour with ID {} deleted successfully.", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to delete colour with ID {}: It is assigned to active products.", id);
            throw new CustomException("Cannot delete colour. It is assigned to active products. Please remove it from those products first.");
        }
    }

    /**
     * Updates an existing colour by ID.
     *
     * @param id Colour ID.
     * @param updatedColour The new colour details.
     * @return The updated colour entity.
     * @throws ResourceNotFoundException If the colour with the given ID does not exist.
     */
    @Transactional
    public Colour updateColour(Long id, Colour updatedColour) {
        logger.info("Updating colour with ID: {}", id);

        // Validate that the colour name is not empty
        if (updatedColour.getName() == null || updatedColour.getName().isEmpty()) {
            logger.error("Updated colour name was empty");
            throw new CustomException("Please provide a colour name.");
        }

        Colour colour = colourRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Failed to update colour: Colour with ID {} not found.", id);
                    return new ResourceNotFoundException("Colour not found with ID: " + id);
                });

        colour.setName(updatedColour.getName());
        Colour updatedEntity = colourRepository.save(colour);

        logger.info("Colour with ID {} updated successfully to: {}", id, updatedEntity.getName());
        return updatedEntity;
    }
}
