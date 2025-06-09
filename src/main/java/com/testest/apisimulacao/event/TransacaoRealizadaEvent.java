package com.testest.apisimulacao.event;

import java.math.BigDecimal;

// Esta classe é um simples contêiner de dados para o evento.
public class TransacaoRealizadaEvent {

    private final Long idContaOrigem;
    private final Long idContaDestino;
    private final BigDecimal valor;

    public TransacaoRealizadaEvent(Long idContaOrigem, Long idContaDestino, BigDecimal valor) {
        this.idContaOrigem = idContaOrigem;
        this.idContaDestino = idContaDestino;
        this.valor = valor;
    }

    // Getters para que o Listener possa acessar os dados
    public Long getIdContaOrigem() {
        return idContaOrigem;
    }

    public Long getIdContaDestino() {
        return idContaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }
}