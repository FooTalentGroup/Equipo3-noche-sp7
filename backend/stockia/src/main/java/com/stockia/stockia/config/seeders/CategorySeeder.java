package com.stockia.stockia.config.seeders;

import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Seeder de categorías usado por el `DataSeeder` principal.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategorySeeder {

    private final ProductCategoryRepository categoryRepository;

    @Transactional
    public void seedCategories() {
        if (categoryRepository.count() > 0) {
            log.info("✅ Las categorías ya existen. Saltando seeding...");
            return;
        }

        log.info("🌱 Iniciando seeding de categorías...");

        List<ProductCategory> categories = List.of(
                ProductCategory.builder()
                        .name("sándwiches")
                        .description("Variedad de sándwiches fríos y calientes: clásicos, gourmets y especiales para llevar.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("omelettes")
                        .description("Omelettes preparados al momento con combinaciones de quesos, vegetales y proteínas.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("rolls")
                        .description("Rolls salados y dulces: opciones frescas y rellenas ideales como snack o entrada.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("tartas")
                        .description("Tartas saladas caseras: masa crocante con rellenos de verduras, carnes o quesos.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("ensaladas")
                        .description("Ensaladas frescas y nutritivas, desde clásicas hasta combinaciones especiales.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("smoothies")
                        .description("Batidos de frutas naturales y cremosos, preparados con yogur o leches vegetales.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("postres")
                        .description("Postres caseros y porciones individuales: tortas, flanes, pudines y más.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("bebidas frías")
                        .description("Bebidas frías: jugos naturales, refrescos, aguas saborizadas y licuados.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("bebidas calientes")
                        .description("Bebidas calientes: café, té, chocolatadas y especialidades calientes.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("budines")
                        .description("Budines caseros en porciones individuales: variedad de sabores dulces y húmedos.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("alfajores")
                        .description("Alfajores tradicionales y rellenos especiales, listos para disfrutar o regalar.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("yoghurt")
                        .description("Yogur natural y saborizado, ideal para acompañar bowls y smoothies.")
                        .isActive(true)
                        .build(),

                ProductCategory.builder()
                        .name("mini tortas")
                        .description("Mini tortas y porciones individuales: perfectas para cafés y celebraciones pequeñas.")
                        .isActive(true)
                        .build()
        );

        List<ProductCategory> saved = categoryRepository.saveAll(categories);
        log.info("✅ Seeding completado: {} categorías creadas", saved.size());
        saved.forEach(c -> log.debug("   → Categoría creada: [ID={}] {}", c.getId(), c.getName()));
    }
}

