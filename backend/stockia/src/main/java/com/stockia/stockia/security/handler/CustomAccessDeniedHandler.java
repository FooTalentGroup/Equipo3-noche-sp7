package com.stockia.stockia.security.handler;

import com.stockia.stockia.exceptions.ErrorResponse;
import com.stockia.stockia.security.constants.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Manejador personalizado para errores de acceso denegado (403 Forbidden).
 * Se activa cuando un usuario autenticado intenta acceder a un recurso para el cual no tiene permisos.
 *
 * IMPORTANTE: Los mensajes NO revelan qué roles específicos se requieren por seguridad.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // Log interno (solo para debugging, no se envía al cliente)
        log.warn("❌ Access denied for user at URI: {} - Reason: {}",
                request.getRequestURI(),
                accessDeniedException.getMessage());

        // Respuesta genérica que NO revela información sobre roles
        ErrorResponse error = new ErrorResponse(
                HttpServletResponse.SC_FORBIDDEN,
                "FORBIDDEN",
                SecurityConstants.Messages.FORBIDDEN,
                List.of("No tienes los permisos necesarios para realizar esta operación"),
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

}

