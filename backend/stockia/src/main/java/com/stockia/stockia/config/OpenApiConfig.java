package com.stockia.stockia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StockIA API")
                        .version("1.0")
                        .description("API para gestión de clientes - Equipo 3 Noche SP7"))
                .servers(List.of(
                        
                        new Server()
                            .url("http://localhost:8080")
                            .description("Desarrollo Local")
                ));
    }
}

/*
* En este archivo deberia implementarse swagger
*
* # Compilar
* mvn clean install
*
* # Ejecutar aplicación
* mvn spring-boot:run
*
* # Tests
* mvn test
*
* # Solo compilar
* mvn compile
*
* # Swagger UI
* http://localhost:8080/swagger-ui.html
*
* # API Endpoints
* http://localhost:8080/api/clientes
*
* # H2 Console
* http://localhost:8080/h2-console
*/
