package com.example.productapp.repository;

import com.example.productapp.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing ProductType entities.
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    boolean existsByName(String name);
}
