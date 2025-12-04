package com.stockia.stockia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de auditoría JPA.
 * Habilita el soporte para las anotaciones @CreatedDate y @LastModifiedDate
 * en las entidades del sistema.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // La anotación @EnableJpaAuditing habilita automáticamente la auditoría
    // para todas las entidades que usen @EntityListeners(AuditingEntityListener.class)
}

