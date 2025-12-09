package com.especlub.match.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/*
 * Author: Anyel EC
 * Github: https://github.com/Anyel-ec
 * Creation date: 10/01/2025
 */

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ESPE  CLUBS MATCH API",
                version = "1.0",
                description = """
                    Proyecto  para encontrar clubs para estudiantes de la ESPE
                    """
        ),
        servers = @Server(url = "http://api.especlubsmatch.app/")
)
public class OpenApiConfig {
}