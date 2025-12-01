package com.stockia.stockia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para notificaciones en tiempo real.
 * Usa el protocolo STOMP sobre WebSocket para enviar notificaciones a los
 * clientes.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el message broker para manejar mensajes STOMP.
     *
     * @param registry registro del message broker
     */
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        // Prefijo para mensajes enviados desde el servidor a los clientes
        registry.enableSimpleBroker("/topic", "/queue");

        // Prefijo para mensajes enviados desde clientes al servidor
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra los endpoints STOMP para el handshake WebSocket.
     *
     * @param registry registro de endpoints STOMP
     */
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
