package com.testest.apisimulacao.dto;

import com.testest.apisimulacao.entity.Conta;
import java.math.BigDecimal;

public record ContaResponseDTO(
        Long id,
        String agencia,
        boolean status,
        BigDecimal saldo
) {
    // Construtor para facilitar a conversão
    public ContaResponseDTO(Conta conta) {
        this(conta.getId(), conta.getAgencia(), conta.isStatus(), conta.getSaldo());
    }
}