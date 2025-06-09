package com.testest.apisimulacao.service;

import com.testest.apisimulacao.dto.NotificacaoStatusDTO;
import com.testest.apisimulacao.dto.TransacaoRequestDTO;
import com.testest.apisimulacao.dto.TransacaoResponseDTO;
import com.testest.apisimulacao.entity.Conta;
import com.testest.apisimulacao.repository.ContaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final NotificacaoService notificacaoService;


    public TransacaoService(ContaRepository contaRepository, NotificacaoService notificacaoService) {
        this.contaRepository = contaRepository;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public void realizarTransacao(TransacaoRequestDTO request) {
        // Busca e valida contas
        Conta contaOrigem = contaRepository.findById(request.idContaOrigem())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada."));
        Conta contaDestino = contaRepository.findById(request.idContaDestino())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada."));

        validarTransacao(contaOrigem, request.agenciaOrigem(), contaDestino, request.agenciaDestino(), request.valor());

        // Atualização de saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));


        List<NotificacaoStatusDTO> notificacoes = new ArrayList<>();

        // Notifica conta de origem e captura a resposta
        String respostaOrigem = notificacaoService.enviarNotificacao("Débito de R$ " + request.valor() + " realizado.");
        notificacoes.add(new NotificacaoStatusDTO(contaOrigem.getAgencia(), respostaOrigem));

        // Notifica conta de destino e captura a resposta
        String respostaDestino = notificacaoService.enviarNotificacao("Crédito de R$ " + request.valor() + " recebido.");
        notificacoes.add(new NotificacaoStatusDTO(contaDestino.getAgencia(), respostaDestino));

        // 4. Monta e retorna a resposta final e completa
        new TransacaoResponseDTO("Transação realizada com sucesso.", notificacoes);
    }

    private void validarTransacao(Conta origem, String agenciaOrigem, Conta destino, String agenciaDestino, BigDecimal valor) {
        if (!origem.isStatus()) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Conta de origem está inativa."); }
        if (!destino.isStatus()) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Conta de destino está inativa."); }
        if (origem.getSaldo().compareTo(valor) < 0) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente."); }
        if (!origem.getAgencia().equalsIgnoreCase(agenciaOrigem)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A agência da conta de origem não confere."); }
        if (!destino.getAgencia().equalsIgnoreCase(agenciaDestino)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A agência da conta de destino não confere."); }
    }
}