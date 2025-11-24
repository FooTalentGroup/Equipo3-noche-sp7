package com.stockia.stockia.config;

import com.stockia.stockia.config.seeders.CategorySeeder;
import com.stockia.stockia.config.seeders.ProductSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeder de datos iniciales para la aplicación.
 * Se ejecuta automáticamente al iniciar la aplicación y crea las categorías
 * base.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CategorySeeder categorySeeder;
    private final ProductSeeder productSeeder;

    /**
     * Ejecuta el seeding de datos al arrancar la aplicación.
     * Solo inserta categorías si la tabla está vacía.
     */
    @Override
    public void run(String... args) {
        // Ejecutar seeders en orden: categorías primero, luego productos
        categorySeeder.seedCategories();
        productSeeder.seedProducts();
    }
}
