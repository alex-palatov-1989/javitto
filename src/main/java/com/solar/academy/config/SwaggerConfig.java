package com.solar.academy.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean public OpenAPI api(){
        return new OpenAPI()
            .info(new Info()
                .title("Default swagger docs")
                .version("1.0.0")
                .description("API-Docs for service Javitto edu-service"));
    }   //  http://localhost:8080/swagger-ui/index.html
}
