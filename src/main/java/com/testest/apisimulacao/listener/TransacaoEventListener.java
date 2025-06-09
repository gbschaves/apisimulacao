package com.testest.apisimulacao.listener;

import com.testest.apisimulacao.entity.Conta;
import com.testest.apisimulacao.event.TransacaoRealizadaEvent;
import com.testest.apisimulacao.repository.ContaRepository;
import com.testest.apisimulacao.service.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TransacaoEventListener {

    private static final Logger log = LoggerFactory.getLogger(TransacaoEventListener.class);

    private final NotificacaoService notificacaoService;
    private final ContaRepository contaRepository;

    public TransacaoEventListener(NotificacaoService notificacaoService, ContaRepository contaRepository) {
        this.notificacaoService = notificacaoService;
        this.contaRepository = contaRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoRealizarTransacao(TransacaoRealizadaEvent event) {

        // A resposta do endpoint é guardada na variável 'respostaNotificacaoOrigem'.
        String respostaNotificacaoOrigem = notificacaoService.enviarNotificacao("Mensagem para origem");


        // Resposta do endpoint que foi guardada na variável.
        contaRepository.findById(event.getIdContaOrigem()).ifPresent(contaOrigem -> {
            contaOrigem.setUltimaNotificacao(respostaNotificacaoOrigem);
            log.info("CONTA ORIGEM {} PREENCHIDA COM: {}", contaOrigem.getId(), respostaNotificacaoOrigem);
        });

        // Resposta do endpoint para a conta de destino
        String respostaNotificacaoDestino = notificacaoService.enviarNotificacao("Mensagem para destino");

        // Preenche o campo da conta de destino
        contaRepository.findById(event.getIdContaDestino()).ifPresent(contaDestino -> {
            contaDestino.setUltimaNotificacao(respostaNotificacaoDestino);
            log.info("CONTA DESTINO {} PREENCHIDA COM: {}", contaDestino.getId(), respostaNotificacaoDestino);
        });
    }
}