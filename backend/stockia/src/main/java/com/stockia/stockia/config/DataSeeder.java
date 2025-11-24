package com.stockia.stockia.config;

import com.stockia.stockia.models.Client;
import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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

        private final ClientRepository clientRepository;
        private final ProductCategoryRepository categoryRepository;

        /**
         * Ejecuta el seeding de datos al arrancar la aplicación.
         * Solo inserta categorías si la tabla está vacía.
         */
        @Override
        public void run(String... args) {
                // Crea las categorías básicas si no existen. (quedarán a cambiar por las de QA)
                seedProductCategories();
                seedClients();
        }

        private void seedClients() {
                if (clientRepository.count() > 0) {
                        log.info("✅ Los clientes ya existen. Saltando seeding...");
                        return;
                }

                log.info("🌱 Iniciando seeding de clientes...");

                List<Client> clients = List.of(
                                Client.builder()
                                                .name("Consumidor Final")
                                                .email("consumidorfinal@stockia.com")
                                                .phone("123456789")
                                                .build());

                clientRepository.saveAll(clients);
                log.info("✅ Seeding completado: {} Cliente consumidor final creado", clients.size());
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
                                                .name("Sandwiches")
                                                .description("Sandwiches")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Omelletes")
                                                .description("Omelletes")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Rolls")
                                                .description("Rolls")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Tartas")
                                                .description("Tartas")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Ensaladas")
                                                .description("Ensaladas")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Smothies")
                                                .description("Smothies")
                                                .isActive(true)
                                                .build(),

                                ProductCategory.builder()
                                                .name("Postres")
                                                .description("Postres")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Bebidas Frías")
                                                .description("Bebidas Frías")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Bebidas Calientes")
                                                .description("Bebidas Calientes")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Budines")
                                                .description("Budines")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Alfajores")
                                                .description("Alfajores")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Yoghurts")
                                                .description("Yoghurts")
                                                .isActive(true)
                                                .build(),
                                ProductCategory.builder()
                                                .name("Mini Tortas")
                                                .description("Mini Tortas")
                                                .isActive(true)
                                                .build());

                // Guardar todas las categorías
                List<ProductCategory> savedCategories = categoryRepository.saveAll(categories);

                log.info("✅ Seeding completado: {} categorías creadas", savedCategories.size());

                // Log de categorías creadas
                savedCategories.forEach(
                                category -> log.debug("   → Categoría creada: [ID={}] {}", category.getId(),
                                                category.getName()));
        }
}
