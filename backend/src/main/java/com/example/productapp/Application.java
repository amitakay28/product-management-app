package com.example.productapp;

import com.example.productapp.model.Colour;
import com.example.productapp.model.ProductType;
import com.example.productapp.repository.ColourRepository;
import com.example.productapp.repository.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Main application entry point for Spring Boot.
 */
@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner loadData(ProductTypeRepository productTypeRepository, ColourRepository colourRepository) {
        return args -> {
            // Load Product Types
            if (productTypeRepository.count() == 0) {
                ProductType furniture = new ProductType(null, "Furniture");
                ProductType electronics = new ProductType(null, "Electronics");
                ProductType clothing = new ProductType(null, "Clothing");

                productTypeRepository.saveAll(List.of(furniture, electronics, clothing));
                productTypeRepository.flush();
                logger.info("Product types loaded successfully.");
            }

            // Load Colours
            if (colourRepository.count() == 0) {
                Colour red = new Colour(null, "Red");
                Colour blue = new Colour(null, "Blue");
                Colour black = new Colour(null, "Black");
                Colour white = new Colour(null, "White");

                colourRepository.saveAll(List.of(red, blue, black, white));
                colourRepository.flush();
                logger.info("Colours loaded successfully.");
            }
        };
    }
}
