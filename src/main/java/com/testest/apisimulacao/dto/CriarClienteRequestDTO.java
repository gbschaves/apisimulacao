package com.testest.apisimulacao.dto;

import com.testest.apisimulacao.entity.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CriarClienteRequestDTO(
        @NotBlank String nome,
        @NotNull TipoDocumento tipoDocumento,
        @NotBlank String documento,
        @NotBlank @Size(min = 6) String senha,

        @NotNull @Valid
        EnderecoRequestDTO endereco,

        @Valid
        List<ContaRequestDTO> contas
) {}