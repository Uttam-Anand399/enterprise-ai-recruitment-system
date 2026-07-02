package com.enterprise.recruitment.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI recruitmentOpenAPI() {

        return new OpenAPI()
        		 .components(
        	                new Components()
        	                        .addSecuritySchemes(
        	                                "Bearer Authentication",

        	                                new SecurityScheme()
        	                                        .type(SecurityScheme.Type.HTTP)
        	                                        .scheme("bearer")
        	                                        .bearerFormat("JWT")
        	                        )
        	        )

        	        .addSecurityItem(
        	                new SecurityRequirement()
        	                        .addList("Bearer Authentication")
        	        )

                .info(new Info()

                        .title("Enterprise AI Recruitment Automation Platform API")

                        .description(
                                "AI Powered Recruitment Management System built using Spring Boot, JWT Authentication, MySQL and Python AI Service."
                        )

                        .version("1.0.0")

                        .contact(
                                new Contact()
                                        .name("Uttam Anand")
                                        .email("uttam04529@gmail.com")
                        )

                        .license(
                                new License()
                                        .name("MIT License")
                        )
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Project Documentation")
                                .url("https://github.com/Uttam-Anand399/enterprise-ai-recruitment-system")
                );

    }

}