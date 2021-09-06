package com.marvel.comicsproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        String notValid = "Не пройдена валидация!";
        String notFound = "Не найден!";
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .useDefaultResponseMessages(false)
            .globalResponses(HttpMethod.GET, Arrays.asList(
                new ResponseBuilder().code("404")
                    .description(notFound).build(),
                new ResponseBuilder().code("400")
                    .description(notValid).build()
            )).globalResponses(HttpMethod.PUT, Arrays.asList(
                new ResponseBuilder().code("400")
                    .description(notValid).build(),
                new ResponseBuilder().code("404")
                    .description(notFound).build()));
    }
}