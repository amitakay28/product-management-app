package com.example.productapp.repository;

import com.example.productapp.model.Colour;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing Colour entities.
 */
public interface ColourRepository extends JpaRepository<Colour, Long> {
}
