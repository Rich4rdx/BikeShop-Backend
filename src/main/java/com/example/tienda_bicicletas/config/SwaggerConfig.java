package com.example.tienda_bicicletas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Tienda de Bicicletas Gastrosena")
                        .version("1.0")
                        .description("Sistema de gestión de inventario, clientes y ventas para el proyecto formativo SENA"));
    }
}
