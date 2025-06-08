package org.tdtu.ecommerceapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// Swagger-ui endpoint: http://localhost:8080/swagger-ui/index.html#
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Final Project - E-commerce API",
                version = "v1.0",
                description = "This is the API documentation for the E-commerce application developed as a final project.",
                contact = @Contact(name = "Nguyen Hoang Danh")),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class OpenApiConfig {
}
