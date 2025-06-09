package com.testest.apisimulacao.controller;

import com.testest.apisimulacao.dto.TransacaoCompletaResponseDTO;
import com.testest.apisimulacao.dto.TransacaoRequestDTO;
import com.testest.apisimulacao.exception.ErrorResponse;
import com.testest.apisimulacao.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @Operation(summary = "Realiza uma transferência entre contas",
            description = "Executa uma transferência monetária de uma conta de origem para uma de destino, validando ID, agência, status e saldo.",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransacaoRequestDTO.class),
                    examples = {
                            @ExampleObject(name = "Sucesso",
                                    summary = "Exemplo de payload para transação bem-sucedida.",
                                    externalValue = "/openapi-examples/transacao-sucesso.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - Saldo Insuficiente",
                                    summary = "Exemplo de payload causara um erro de saldo insuficiente 422.",
                                    externalValue = "/openapi-examples/transacao-erro-saldo-insuficiente.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - Conta Inativa",
                                    summary = "Exemplo de payload causara um erro de conta inativa 422.",
                                    externalValue = "/openapi-examples/transacao-erro-conta-inativa.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - Conta não Encontrada",
                                    summary = "Exemplo de payload causara um conta não encontrada 404.",
                                    externalValue = "/openapi-examples/transacao-erro-conta-nao-encontrada.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - Agência Inválida",
                                    summary = "Exemplo de payload causara um erro de agência inválida 400.",
                                    externalValue = "/openapi-examples/transacao-erro-agencia-invalida.json")
                    }))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: agência não confere, valor negativo)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (ex: saldo insuficiente, conta inativa)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/transacoes/realizar")
    public ResponseEntity<TransacaoCompletaResponseDTO> realizarTransacao(@Valid @org.springframework.web.bind.annotation.RequestBody TransacaoRequestDTO request) {
        TransacaoCompletaResponseDTO response = transacaoService.realizarTransacao(request);
        return ResponseEntity.ok(response);
    }
}