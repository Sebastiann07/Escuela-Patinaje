package com.patinaje.v1.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Escuela de Patinaje API",
                version = "v1",
                description = "API para gestionar usuarios, clases, instructores y asistencias",
                contact = @Contact(name = "Equipo", email = "soporte@example.com"),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "/", description = "Local server")
        }
)
public class OpenApiConfig {

}
