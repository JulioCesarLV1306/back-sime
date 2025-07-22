package com.sime.backwebsime.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desarrollo"),
                        new Server().url("https://back-sime.onrender.com").description("Servidor de Producción en Render")
                ))
                .info(new Info()
                        .title("SIME - Sistema de Matrículas Escolares")
                        .description("API REST para el manejo de matrículas, alumnos, apoderados y consulta de DNI")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo SIME")
                                .email("contacto@sime.com")
                                .url("https://github.com/JulioCesarLV1306/back-sime"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
