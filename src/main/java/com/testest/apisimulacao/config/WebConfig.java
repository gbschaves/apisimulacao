package com.testest.apisimulacao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                // O padrão de URL
                .addResourceHandler("/openapi-examples/**")
                // Local onde os arquivos
                .addResourceLocations("classpath:/openapi-examples/");
    }
}