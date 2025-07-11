package com.app.playerservicejava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Player Service API")
                        .version("1.0")
                        .description("OpenAPI documentation for the Player Service Java backend."));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        String apiVersion = System.getProperty("api.version", "v1");
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**", "/" + apiVersion + "/players/**")
                .build();
    }
}
