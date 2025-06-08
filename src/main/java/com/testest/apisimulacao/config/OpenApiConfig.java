package com.testest.apisimulacao.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Configurações Swagger
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Simulação")
                        .description("Esta api é foi desenvolvida com o intuito de simular um cadastro de um cliente e transações bancárias")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Guilherme Barbosa Chaves da Silva")
                                .url("github.com/gbschaves/apisimulacao")
                                .email("gb.chaves@hotmail.com"))
                        .license(new License()
                                .name("Licença da API")
                                .url("http://springdoc.org")));
    }
}