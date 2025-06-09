package com.testest.apisimulacao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);
    private final RestTemplate restTemplate;

    public NotificacaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String enviarNotificacao(String mensagem) {
        try {
            log.info("Enviando notificação: {}", mensagem);
            String MOCK_NOTIFICATION_URL = "https://run.mocky.io/v3/e4520707-3550-4022-9d34-88c3b38e0b28";
            ResponseEntity<String> response = restTemplate.getForEntity(MOCK_NOTIFICATION_URL, String.class);

            // retorna a resposta de sucesso
            return response.getBody();

        } catch (RestClientException e) {
            // Retorna a resposta de falha

            log.error("FALHA AO ENVIAR NOTIFICAÇÃO, SERVIÇO INDISPONÍVEL: {}", e.getMessage());

            // Retorna uma mensagem de erro específica.
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, // Código de erro 503
                    "Serviço de notificação indisponível. A transação foi cancelada."
            );
        }
    }
}
