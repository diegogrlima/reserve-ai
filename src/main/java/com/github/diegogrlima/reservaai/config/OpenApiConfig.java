package com.github.diegogrlima.reservaai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Reserve-ai API",
                version = "1.0.0",
                description = "API para gestao de usuarios, quartos e reservas em um sistema de hospedagem.",
                contact = @Contact(name = "Diego Lima")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local")
        }
)
public class OpenApiConfig {
}
