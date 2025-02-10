package com.example.productapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for returning detailed product information.
 */
@Data
@AllArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private String productType;
    private String colours; // Comma-separated list of colour names
}
