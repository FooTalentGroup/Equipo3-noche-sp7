package com.stockia.stockia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class OpenApiConfig {

    @Bean
    @Profile("dev")
    public OpenAPI customOpenApiDev() {
        return new OpenAPI()
                .info(new Info()
                        .title("StockIA API")
                        .version("1.0")
                        .description("API para gestión de inventario - Equipo 3 Noche SP7"))
                .addServersItem(new Server().url("http://localhost:8080").description("Development Server"));
    }

    @Bean
    @Profile("prod")
    public OpenAPI customOpenApiProd() {
        return new OpenAPI()
                .addServersItem(new Server().url("").description("Production Server"));
    }
}