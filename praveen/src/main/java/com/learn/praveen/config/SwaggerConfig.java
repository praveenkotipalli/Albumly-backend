package com.learn.praveen.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Praveen API",
        version = "1.0",
        contact = @Contact(
            name = "Praveen",
            email = "2200032132cseh@gmail.com",
            url = "https://www.linkedin.com/in/praveenkumarkotipalli/"
        ),
        description = "Praveen API Documentation"
    )
)
public class SwaggerConfig {
    
}
