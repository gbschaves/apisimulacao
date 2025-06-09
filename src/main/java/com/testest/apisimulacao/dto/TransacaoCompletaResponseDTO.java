package com.testest.apisimulacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransacaoCompletaResponseDTO {
    private String responseServicoNotificacao;
    private String resumo;
    private List<ContaDetalheDTO> contasAtualizadas;
}