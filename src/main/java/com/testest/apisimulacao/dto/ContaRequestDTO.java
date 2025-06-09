package com.testest.apisimulacao.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ContaRequestDTO(
        @NotBlank String agencia,
        @NotNull BigDecimal saldoInicial,
        Boolean status
) {}