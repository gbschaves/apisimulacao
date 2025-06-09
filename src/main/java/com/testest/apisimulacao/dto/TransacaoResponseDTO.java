package com.testest.apisimulacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransacaoResponseDTO {
    private String mensagem;
    private List<NotificacaoStatusDTO> notificacoes;
}