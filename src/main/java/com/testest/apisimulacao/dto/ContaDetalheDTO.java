package com.testest.apisimulacao.dto;

import com.testest.apisimulacao.entity.Conta;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ContaDetalheDTO {
    private Long id;
    private String agencia;
    private boolean status;
    private BigDecimal saldo;
    private String ultimaNotificacao;

    public ContaDetalheDTO(Conta conta) {
        this.id = conta.getId();
        this.agencia = conta.getAgencia();
        this.status = conta.isStatus();
        this.saldo = conta.getSaldo();
        this.ultimaNotificacao = conta.getUltimaNotificacao();
    }
}