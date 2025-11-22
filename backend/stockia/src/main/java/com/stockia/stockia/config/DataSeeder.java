package com.stockia.stockia.config;

import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeder de datos iniciales para la aplicación.
 * Se ejecuta automáticamente al iniciar la aplicación y crea las categorías base.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProductCategoryRepository categoryRepository;

    /**
     * Ejecuta el seeding de datos al arrancar la aplicación.
     * Solo inserta categorías si la tabla está vacía.
     */
    @Override
    public void run(String... args) {
//        Crea las categorías básicas si no existen. (quedarán a cambiar por las de QA)
//        seedProductCategories();
    }

    /**
     * Crea las categorías base si no existen.
     */
    private void seedProductCategories() {
        // Verificar si ya existen categorías
        if (categoryRepository.count() > 0) {
            log.info("✅ Las categorías ya existen. Saltando seeding...");
            return;
        }

        log.info("🌱 Iniciando seeding de categorías...");

        // Crear lista de categorías iniciales
        List<ProductCategory> categories = List.of(
            ProductCategory.builder()
                .name("Electrónica")
                .description("Dispositivos electrónicos, computadoras y accesorios tecnológicos")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Oficina")
                .description("Suministros, equipos y materiales de oficina")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Muebles")
                .description("Mobiliario para oficina, hogar y espacios comerciales")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Alimentos")
                .description("Productos alimenticios y bebidas")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Limpieza")
                .description("Productos de limpieza, higiene y mantenimiento")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Herramientas")
                .description("Herramientas manuales y eléctricas para construcción y reparación")
                .isActive(true)
                .build(),

            ProductCategory.builder()
                .name("Papelería")
                .description("Artículos de papelería, escritorio y escolares")
                .isActive(true)
                .build()
        );

        // Guardar todas las categorías
        List<ProductCategory> savedCategories = categoryRepository.saveAll(categories);

        log.info("✅ Seeding completado: {} categorías creadas", savedCategories.size());

        // Log de categorías creadas
        savedCategories.forEach(category ->
            log.debug("   → Categoría creada: [ID={}] {}", category.getId(), category.getName())
        );
    }
}

