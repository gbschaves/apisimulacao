package com.testest.apisimulacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransacaoRequestDTO(
        @NotNull Long idContaOrigem,
        @NotBlank String agenciaOrigem,
        @NotNull Long idContaDestino,
        @NotBlank String agenciaDestino,
        @NotNull @Positive BigDecimal valor
) {}