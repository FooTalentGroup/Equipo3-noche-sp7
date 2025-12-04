package com.stockia.stockia.config.seeders;

import com.stockia.stockia.models.Product;
import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import com.stockia.stockia.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Seeder de productos de ejemplo coherentes con las categorías existentes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSeeder {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    @Transactional
    public void seedProducts() {
        if (productRepository.count() > 0) {
            log.info("✅ Los productos ya existen. Saltando seeding...");
            return;
        }

        log.info("🌱 Iniciando seeding de productos de ejemplo...");

        List<Product> products = new ArrayList<>();

        // Lista de ejemplos: nombre, categoría, precio, stock, photoUrl
        addProduct(products, "Sándwich de Jamón y Queso", "sándwiches", 350.00, 20, "https://hips.hearstapps.com/hmg-prod/images/panini-sandwiches-royalty-free-image-1588773746.jpg?crop=1xw:0.84355xh;center,top&resize=1200:*");
        addProduct(products, "Omelette de Jamón y Queso", "omelettes", 420.00, 15, "https://static.vecteezy.com/system/resources/thumbnails/071/693/003/small/delicious-omelet-with-ham-and-parsley-on-white-plate-with-transparent-background-png.png");
        addProduct(products, "Roll de Pollo César", "rolls", 480.00, 12, "https://www.shutterstock.com/image-photo/delicious-homemade-fresh-chicken-caesar-600nw-1428871739.jpg");
        addProduct(products, "Tarta de Espinaca y Ricota (porción)", "tartas", 650.00, 8, "https://resizer.glanacion.com/resizer/v2/tarta-de-HNXNGD4G2RBMJABKQXMSBKFFQQ.jpg?auth=4d5ca27e485935b30a21ab9d929e6f243a21a8e8ca599fe065d0d30d6e9e693d&width=420&height=280&quality=70&smart=true");
        addProduct(products, "Ensalada César Clásica", "ensaladas", 530.00, 10, "https://sarasellos.com/wp-content/uploads/2024/07/ensalada-cesar1.jpg");
        addProduct(products, "Smoothie Frutal Tropical 400ml", "smoothies", 450.00, 18, "https://buenprovecho.hn/wp-content/uploads/2019/01/Smoothie-tropical-1.jpg");
        addProduct(products, "Porción de Cheesecake", "postres", 520.00, 9, "https://acdn-us.mitiendanube.com/stores/423/649/products/_dsc3031-11-394f83be0b486ddd4716898645363792-1024-1024.jpg");
        addProduct(products, "Jugo Natural Naranja 500ml", "bebidas frías", 240.00, 30, "https://dorothys.farm/wp-content/uploads/2024/08/JU-JN-500ml-005-jugo-naranja-06-900x900.jpg");
        addProduct(products, "Café Americano", "bebidas calientes", 180.00, 50, "https://www.somoselcafe.com.ar/img/novedades/47.jpg");
        addProduct(products, "Budín de Banana (individual)", "budines", 300.00, 14, "https://www.bairesgourmet.com/dobyt/contenido/noticias/original/1743772213.jpeg");
        addProduct(products, "Alfajor Clásico", "alfajores", 120.00, 40, "https://leonardoespinoza.com/cdn/shop/files/AlfajoresMarplatense.jpg?v=1731972145");
        addProduct(products, "Mini Torta Red Velvet", "mini tortas", 700.00, 6, "https://acdn-us.mitiendanube.com/stores/413/750/products/20250719_115710-db0935a6e088f3c85c17539158023076-480-0.jpg");
        // Si quieres, puedes agregar un producto para "Yoghurt" aquí.

        if (products.isEmpty()) {
            log.warn("⚠️ No se crearon productos porque no se encontraron categorías asociadas.");
            return;
        }

        List<Product> saved = productRepository.saveAll(products);
        log.info("✅ Seeding de productos completado: {} productos creados", saved.size());
        saved.forEach(p -> log.debug("   → Producto creado: [ID={}] {} (cat={})", p.getId(), p.getName(), p.getCategory().getName()));
    }

    private void addProduct(List<Product> products, String name, String categoryName, double price, int stock, String photoUrl) {
        Optional<ProductCategory> catOpt = categoryRepository.findByNameIgnoreCase(categoryName);
        if (catOpt.isEmpty()) {
            log.warn("   ✖ Categoría no encontrada: '{}' — producto '{}' omitido", categoryName, name);
            return;
        }

        ProductCategory category = catOpt.get();

        // Normalizar nombre a lowercase para consistencia
        String normalizedName = name.trim().toLowerCase();

        Product product = Product.builder()
                .name(normalizedName)
                .category(category)
                .price(BigDecimal.valueOf(price))
                .photoUrl(photoUrl)
                .currentStock(stock)
                .minStock(5)
                .isAvailable(true)
                .build();

        products.add(product);
    }
}

