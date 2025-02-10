package com.example.productapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO for returning detailed product information.
 */
@Data
@AllArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private String productType;
    private List<String> colours;
}
