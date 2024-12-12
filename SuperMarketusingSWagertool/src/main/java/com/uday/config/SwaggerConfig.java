package com.uday.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supermarket Register API")
                        .description("API documentation for managing bills and items in the Supermarket Register system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Uday Kiran Annadasu")
                                .email("udaykiranannadasu@gmail.com")
                                .url("https://github.com/UdayKiran965"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
