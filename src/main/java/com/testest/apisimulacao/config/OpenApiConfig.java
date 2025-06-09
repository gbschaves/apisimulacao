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
        String description = "Esta API foi desenvolvida para simular um cadastro de cliente e transações bancárias."
                + "<br><br><strong>Instruções de Demonstração:</strong><br>"
                + "1. Utilize primeiro o endpoint de <strong>cadastro de cliente</strong>.<br>"
                + "2. Em seguida, utilize o endpoint de <strong>cadastro de conta</strong> para o cliente criado.<br>"
                + "3. Com o cliente e a conta prontos, os payloads de <strong>transações</strong> funcionarão como esperado."
                + "4. Para realizar o teste de falha na transação por parte do serviço externo, apague algum algoritmo do endpoint dentro da classe <strong>NotificacaoService</strong>.<br>";

        return new OpenAPI()
                .info(new Info()
                        .title("API de Simulação Bancária")
                        .description(description)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Guilherme Barbosa Chaves da Silva")
                                .url("https://github.com/gbschaves/apisimulacao")
                                .email("gb.chaves@hotmail.com"))
                        .license(new License()
                                .name("Licença da API")
                                .url("https://springdoc.org")));
    }
}