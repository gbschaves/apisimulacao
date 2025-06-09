package com.testest.apisimulacao.dto;

import com.testest.apisimulacao.entity.Cliente;
import com.testest.apisimulacao.entity.TipoDocumento;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record ClienteResponseDTO(
        Long id,
        String nome,
        TipoDocumento tipoDocumento,
        String documento,
        List<ContaResponseDTO> contas,
        String mensagem
) {

    public ClienteResponseDTO(Cliente cliente, String mensagem) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTipoDocumento(),
                cliente.getDocumento(),
                cliente.getContas() != null ? cliente.getContas().stream().map(ContaResponseDTO::new).collect(Collectors.toList()) : Collections.emptyList(),
                mensagem
        );
    }

    public ClienteResponseDTO(Cliente cliente) {
        this(cliente, null);
    }
}