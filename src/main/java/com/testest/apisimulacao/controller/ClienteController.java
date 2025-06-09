package com.testest.apisimulacao.controller;

import com.testest.apisimulacao.dto.ClienteResponseDTO;
import com.testest.apisimulacao.dto.CriarClienteRequestDTO;
import com.testest.apisimulacao.exception.ErrorResponse;
import com.testest.apisimulacao.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Cadastrar um novo cliente",
            description = "Cria um novo cliente com endereço e contas iniciais.",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CriarClienteRequestDTO.class),
                    examples = {

                            // Exemplos de CPF
                            @ExampleObject(name = "Sucesso - Cadastro CPF",

                                    summary = "Exemplo de payload para cadastrar um cliente CPF.",

                                    externalValue = "/openapi-examples/cliente-cpf-sucesso.json"),

                            @ExampleObject(name = "Exemplo de payload com com erro - CPF com tamanho inválido",

                                    summary = "Exemplo que causará um erro de tamanho de CPF 400 (Bad Request).",

                                    externalValue = "/openapi-examples/cliente-cpf-erro-tamanho.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - CPF inválido",

                                    summary = "Exemplo que causará um erro de CPF inválido 400 (Bad Request).",

                                    externalValue = "/openapi-examples/cliente-cpf-erro-invalido.json"),

                            // Exemplos de CNPJ
                            @ExampleObject(name = "Sucesso - Cadastro CNPJ",

                                    summary = "Exemplo de payload para cadastrar um cliente CNPJ.",

                                    externalValue = "/openapi-examples/cliente-cnpj-sucesso.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - CNPJ com tamanho inválido",

                                    summary = "Exemplo que causará um erro de tamanho de CNPJ 400 (Bad Request).",

                                    externalValue = "/openapi-examples/cliente-cnpj-erro-tamanho.json"),

                            @ExampleObject(name = "Exemplo de payload com erro - CNPJ inválido",

                                    summary = "Exemplo que causará um erro de CNPJ inválido 400 (Bad Request).",

                                    externalValue = "/openapi-examples/cliente-cnpj-erro-invalido.json"),
                            @ExampleObject(name = "Exemplo de payload para cadastrar um cliente CNPJ com uma conta inativa.",

                                    summary = "Exemplo de payload para cadastrar um cliente CNPJ.",

                                    externalValue = "/openapi-examples/cliente-cnpj-conta-inativa-sucesso.json"),

                    }))
    )

    // Responses
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class)) }),

            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "409", description = "Conflito de dados, documento já cadastrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/cadastro/clientes")
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(@Valid @org.springframework.web.bind.annotation.RequestBody CriarClienteRequestDTO requestDTO) {
        ClienteResponseDTO clienteCriado = clienteService.cadastrarCliente(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }

    @GetMapping("/listar/clientes")
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }
}