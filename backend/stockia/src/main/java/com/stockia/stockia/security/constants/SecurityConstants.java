package com.stockia.stockia.security.constants;

/**
 * Constantes de seguridad centralizadas para roles y expresiones SpEL.
 * Evita duplicación de strings en anotaciones @PreAuthorize.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-23
 */
public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }

    /**
     * Expresiones SpEL para @PreAuthorize
     */
    public static final class Roles {
        public static final String ADMIN_ONLY = "hasRole('ADMIN')";
        public static final String MANAGER_ONLY = "hasRole('MANAGER')";
        public static final String ADMIN_OR_MANAGER = "hasAnyRole('ADMIN', 'MANAGER')";
        public static final String AUTHENTICATED = "isAuthenticated()";

        private Roles() {
            throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
        }
    }

    /**
     * Mensajes de respuesta de seguridad
     */
    public static final class Messages {
        public static final String UNAUTHORIZED = "Acceso no autorizado. Token inválido o ausente";
        public static final String FORBIDDEN = "Acceso denegado. No tienes permisos para realizar esta acción";

        private Messages() {
            throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
        }
    }
}

