package com.sharath.loanpro.loanservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("LoanPro Loan Service API")
                        .description("Microservice APIs for Loan Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Sharath Reddy")
                                .email("sharathreddysuram@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("LoanPro Documentation"));
    }
}