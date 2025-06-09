package com.testest.apisimulacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EnderecoRequestDTO(
        @NotBlank String endereco
//        ,@NotBlank String rua,
//        @NotBlank String numero,
//        String complemento,
//        @NotBlank String bairro,
//        @NotBlank String cidade,
//        @NotBlank String uf,
//        @NotBlank String cep
) {}