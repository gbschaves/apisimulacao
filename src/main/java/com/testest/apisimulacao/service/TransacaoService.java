package com.testest.apisimulacao.service;

import com.testest.apisimulacao.dto.ContaDetalheDTO;
import com.testest.apisimulacao.dto.TransacaoCompletaResponseDTO;
import com.testest.apisimulacao.dto.TransacaoRequestDTO;
import com.testest.apisimulacao.entity.Conta;
import com.testest.apisimulacao.repository.ContaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final NotificacaoService notificacaoService;

    public TransacaoService(ContaRepository contaRepository, NotificacaoService notificacaoService) {
        this.contaRepository = contaRepository;
        this.notificacaoService = notificacaoService;
    }

    // Utilizado transactional para que a operação possa ser revertida
    @Transactional
    public TransacaoCompletaResponseDTO realizarTransacao(TransacaoRequestDTO request) {
        Conta contaOrigem = contaRepository.findById(request.idContaOrigem())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada."));
        Conta contaDestino = contaRepository.findById(request.idContaDestino())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada."));

        validarTransacao(contaOrigem, request.agenciaOrigem(), contaDestino, request.agenciaDestino(), request.valor());

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));

        // Preenche o campo ultimaNotificacao nos objetos para notificar as contas que fizeram transações
        String respostaOrigem = notificacaoService.enviarNotificacao("Débito de R$ " + request.valor() + " realizado.");
        contaOrigem.setUltimaNotificacao(respostaOrigem);

        String respostaDestino = notificacaoService.enviarNotificacao("Crédito de R$ " + request.valor() + " recebido.");
        contaDestino.setUltimaNotificacao(respostaDestino);

        // Constroi a resposta detalhada
        String responseServico = "Respostas do serviço de notificação capturadas com sucesso.";
        String resumo = String.format("As contas das agências '%s' e '%s' foram notificadas.", contaOrigem.getAgencia(), contaDestino.getAgencia());

        List<ContaDetalheDTO> contasAtualizadas = Stream.of(contaOrigem, contaDestino)
                .map(ContaDetalheDTO::new)
                .collect(Collectors.toList());

        // Retorna o objeto de resposta
        return new TransacaoCompletaResponseDTO(responseServico, resumo, contasAtualizadas);
    }

    private void validarTransacao(Conta origem, String agenciaOrigem, Conta destino, String agenciaDestino, BigDecimal valor) {
        if (!origem.isStatus()) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Conta de origem está inativa."); }
        if (!destino.isStatus()) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Conta de destino está inativa."); }
        if (origem.getSaldo().compareTo(valor) < 0) { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente."); }
        if (!origem.getAgencia().equalsIgnoreCase(agenciaOrigem)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A agência da conta de origem não confere."); }
        if (!destino.getAgencia().equalsIgnoreCase(agenciaDestino)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A agência da conta de destino não confere."); }
    }
}